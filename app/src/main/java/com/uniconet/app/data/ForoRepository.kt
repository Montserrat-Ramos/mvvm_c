package com.uniconet.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uniconet.app.network.NetworkModule
import com.uniconet.app.network.dto.AddComentarioRequest
import com.uniconet.app.network.dto.PublishAnuncioRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private val Context.dataStoreForo by preferencesDataStore(name = "uniconet_foro")

// Sincroniza anuncios con la API; mantiene borradores/likes/comentarios locales cuando aplica.
class ForoRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val key = stringPreferencesKey("anuncios_json")
    private val keyComentarios = stringPreferencesKey("comentarios_json")
    private val api = NetworkModule.createApi()

    val anuncios: Flow<List<Anuncio>> = context.dataStoreForo.data.map { prefs ->
        val s = prefs[key]
        if (s.isNullOrBlank()) emptyList() else decode(s)
    }

    val comentarios: Flow<List<ComentarioForo>> = context.dataStoreForo.data.map { prefs ->
        val s = prefs[keyComentarios]
        if (s.isNullOrBlank()) emptyList() else decodeComentarios(s)
    }

    suspend fun refresh() {
        runCatching {
            val remotos = api.getAnuncios().map { dto ->
                Anuncio(
                    id = dto.id,
                    autor = dto.autor.ifBlank { "Profesor · Maestro" },
                    fecha = dto.fecha.ifBlank { "Hoy" },
                    titulo = dto.titulo,
                    contenido = dto.contenido,
                    destinatarios = dto.destinatarios,
                    likes = dto.likes,
                    meGusta = false,
                    borrador = dto.borrador,
                    archivoNombre = dto.archivoNombre
                )
            }
            val locales = current()
            val borradoresLocales = locales.filter { it.borrador }
            val likesLocales = locales.associate { it.id to Pair(it.likes, it.meGusta) }
            val merged = remotos.map { r ->
                val local = likesLocales[r.id]
                if (local != null) r.copy(likes = local.first, meGusta = local.second) else r
            }
            save(borradoresLocales + merged)
        }
    }

    suspend fun publicar(titulo: String, contenido: String, destinatarios: String, archivoNombre: String = "") {
        val ok = runCatching {
            api.publishAnuncio(
                PublishAnuncioRequest(
                    titulo = titulo.trim(),
                    contenido = contenido.trim(),
                    destinatarios = destinatarios.trim(),
                    archivoNombre = archivoNombre.trim()
                )
            )
        }.isSuccess
        if (ok) {
            refresh()
        } else {
            val base = current()
            val nuevo = Anuncio(
                id = "a_${System.currentTimeMillis()}",
                autor = "Profesor · Maestro",
                fecha = "Hoy",
                titulo = titulo.trim(),
                contenido = contenido.trim(),
                destinatarios = destinatarios.trim(),
                likes = 0,
                meGusta = false,
                borrador = false,
                archivoNombre = archivoNombre.trim()
            )
            save(listOf(nuevo) + base)
        }
    }

    suspend fun guardarBorrador(titulo: String, contenido: String, destinatarios: String, archivoNombre: String = "") {
        val base = current()
        val nuevo = Anuncio(
            id = "a_${System.currentTimeMillis()}",
            autor = "Borrador",
            fecha = "Hoy",
            titulo = titulo.trim(),
            contenido = contenido.trim(),
            destinatarios = destinatarios.trim(),
            likes = 0,
            borrador = true,
            archivoNombre = archivoNombre.trim()
        )
        save(listOf(nuevo) + base)
    }

    suspend fun eliminar(id: String) {
        val ok = runCatching { api.deleteAnuncio(id) }.isSuccess
        if (ok) refresh() else save(current().filter { it.id != id })
    }

    suspend fun toggleLike(id: String) {
        val updated = current().map {
            if (it.id == id) it.copy(meGusta = !it.meGusta, likes = it.likes + if (!it.meGusta) 1 else -1) else it
        }
        save(updated)
    }

    suspend fun agregarComentario(anuncioId: String, autor: String, texto: String) {
        val ok = runCatching {
            api.addComentario(anuncioId, AddComentarioRequest(autor = autor, texto = texto.trim()))
        }.isSuccess
        if (!ok) {
            val prefs = context.dataStoreForo.data.first()
            val s = prefs[keyComentarios]
            val base = if (s.isNullOrBlank()) emptyList() else decodeComentarios(s)
            val nuevo = ComentarioForo(
                id = "c_${System.currentTimeMillis()}",
                anuncioId = anuncioId,
                autor = autor,
                texto = texto.trim()
            )
            context.dataStoreForo.edit { p -> p[keyComentarios] = encodeComentarios(base + nuevo) }
        }
    }

    suspend fun eliminarComentario(id: String) {
        val prefs = context.dataStoreForo.data.first()
        val s = prefs[keyComentarios]
        val base = if (s.isNullOrBlank()) emptyList() else decodeComentarios(s)
        context.dataStoreForo.edit { p -> p[keyComentarios] = encodeComentarios(base.filter { it.id != id }) }
    }

    private suspend fun current(): List<Anuncio> {
        val prefs = context.dataStoreForo.data.first()
        val s = prefs[key]
        return if (s.isNullOrBlank()) emptyList() else decode(s)
    }

    private suspend fun save(list: List<Anuncio>) {
        context.dataStoreForo.edit { p -> p[key] = encode(list) }
    }

    private fun decode(s: String): List<Anuncio> =
        runCatching { json.decodeFromString(ListSerializer(Anuncio.serializer()), s) }.getOrDefault(emptyList())

    private fun decodeComentarios(s: String): List<ComentarioForo> =
        runCatching { json.decodeFromString(ListSerializer(ComentarioForo.serializer()), s) }.getOrDefault(emptyList())

    private fun encodeComentarios(list: List<ComentarioForo>): String =
        json.encodeToString(ListSerializer(ComentarioForo.serializer()), list)

    private fun encode(list: List<Anuncio>): String =
        json.encodeToString(ListSerializer(Anuncio.serializer()), list)
}
