package com.uniconet.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uniconet.app.network.NetworkModule
import com.uniconet.app.network.dto.UploadBookRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "uniconet_books")

// Sincroniza con la API de AWS cuando está disponible y mantiene caché local.
class BookRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val keyBooks = stringPreferencesKey("books_json")
    private val keyDownloads = stringPreferencesKey("downloads_json")
    private val api = NetworkModule.createApi()

    // Libros con estado de descarga (local por dispositivo)
    val libros: Flow<List<Book>> = context.dataStore.data.map { prefs ->
        val booksJson = prefs[keyBooks]
        val downloadsJson = prefs[keyDownloads]
        val base = if (booksJson.isNullOrBlank()) emptyList() else decodeBooks(booksJson)
        val downloadedIds = decodeDownloads(downloadsJson)
        base.map { it.copy(descargado = downloadedIds.contains(it.id)) }
    }

    // Obtiene desde el servidor y actualiza caché local.
    suspend fun refresh() {
        runCatching {
            val remotos = api.getBooks().map { dto ->
                Book(
                    id = dto.id,
                    titulo = dto.titulo,
                    autor = dto.autor,
                    categoria = dto.categoria.ifBlank { "General" },
                    estado = dto.estado.ifBlank { "Disponible" },
                    paginas = dto.paginas,
                    archivoNombre = dto.archivoNombre
                )
            }
            context.dataStore.edit { prefs ->
                prefs[keyBooks] = encodeBooks(remotos)
            }
        }
    }

    suspend fun subirLibro(titulo: String, autor: String, categoria: String, archivoNombre: String = "") {
        val ok = runCatching {
            api.uploadBook(
                UploadBookRequest(
                    titulo = titulo.trim(),
                    autor = autor.trim().ifBlank { "---" },
                    categoria = categoria.trim().ifBlank { "General" },
                    archivoNombre = archivoNombre.trim()
                )
            )
        }.isSuccess

        if (ok) {
            refresh()
        } else {
            // Fallback local si no hay conexión
            val base = currentBooks()
            val nuevo = Book(
                id = "local_${base.size + 1}_${System.currentTimeMillis()}",
                titulo = titulo.trim(),
                autor = autor.trim().ifBlank { "---" },
                categoria = categoria.trim().ifBlank { "General" },
                estado = "Disponible",
                paginas = "",
                archivoNombre = archivoNombre.trim()
            )
            context.dataStore.edit { prefs ->
                prefs[keyBooks] = encodeBooks(listOf(nuevo) + base)
            }
        }
    }

    suspend fun marcarDescargado(id: String) {
        val prefs = context.dataStore.data.first()
        val ids = decodeDownloads(prefs[keyDownloads]).toMutableSet()
        ids.add(id)
        context.dataStore.edit { p ->
            p[keyDownloads] = encodeDownloads(ids)
        }
    }

    suspend fun eliminarLibro(id: String) {
        val ok = runCatching { api.deleteBook(id) }.isSuccess
        if (ok) {
            refresh()
        } else {
            val base = currentBooks().filter { it.id != id }
            context.dataStore.edit { prefs ->
                prefs[keyBooks] = encodeBooks(base)
            }
        }
    }

    private suspend fun currentBooks(): List<Book> {
        val prefs = context.dataStore.data.first()
        val jsonStr = prefs[keyBooks]
        return if (jsonStr.isNullOrBlank()) emptyList() else decodeBooks(jsonStr)
    }

    private fun decodeBooks(s: String): List<Book> = runCatching {
        json.decodeFromString(ListSerializer(Book.serializer()), s)
    }.getOrDefault(emptyList())

    private fun encodeBooks(list: List<Book>): String =
        json.encodeToString(ListSerializer(Book.serializer()), list)

    private fun decodeDownloads(s: String?): Set<String> {
        if (s.isNullOrBlank()) return emptySet()
        return runCatching {
            json.decodeFromString(ListSerializer(String.serializer()), s).toSet()
        }.getOrDefault(emptySet())
    }

    private fun encodeDownloads(ids: Set<String>): String =
        json.encodeToString(ListSerializer(String.serializer()), ids.toList())
}
