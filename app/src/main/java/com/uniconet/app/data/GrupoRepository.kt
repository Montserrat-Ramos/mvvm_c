package com.uniconet.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uniconet.app.network.NetworkModule
import com.uniconet.app.network.dto.CreateGrupoRequest
import com.uniconet.app.network.dto.GrupoActionRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private val Context.dataStoreGrupos by preferencesDataStore(name = "uniconet_grupos")

// Sincroniza grupos especiales con la API de AWS.
class GrupoRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val key = stringPreferencesKey("grupos_json")
    private val api = NetworkModule.createApi()

    val grupos: Flow<List<GrupoEspecial>> = context.dataStoreGrupos.data.map { prefs ->
        val s = prefs[key]
        if (s.isNullOrBlank()) emptyList() else decode(s)
    }

    suspend fun refresh() {
        runCatching {
            val remotos = api.getGrupos().map { dto ->
                GrupoEspecial(
                    id = dto.id,
                    nombre = dto.nombre,
                    integrantes = dto.integrantes,
                    estado = dto.estado,
                    descripcion = dto.descripcion,
                    fecha = dto.fecha,
                    maestro = dto.maestro
                )
            }
            save(remotos)
        }
    }

    suspend fun crear(nombre: String, integrantes: String, descripcion: String, fecha: String, maestro: String) {
        val ok = runCatching {
            api.createGrupo(
                CreateGrupoRequest(
                    nombre = nombre.trim(),
                    integrantes = integrantes.trim().ifBlank { "1 estudiante" },
                    descripcion = descripcion.trim().ifBlank { "Información del grupo pendiente." },
                    fecha = fecha.trim().ifBlank { "Pendiente de definir" },
                    maestro = maestro.trim().ifBlank { "Sin asignación registrada" }
                )
            )
        }.isSuccess
        if (ok) refresh() else {
            val base = current()
            val nuevo = GrupoEspecial(
                id = "g_${System.currentTimeMillis()}",
                nombre = nombre.trim(),
                integrantes = integrantes.trim().ifBlank { "1 estudiante" },
                estado = "Pendiente",
                descripcion = descripcion.trim().ifBlank { "Información del grupo pendiente." },
                fecha = fecha.trim().ifBlank { "Pendiente de definir" },
                maestro = maestro.trim().ifBlank { "Sin asignación registrada" }
            )
            save(listOf(nuevo) + base)
        }
    }

    suspend fun eliminar(id: String) {
        val ok = runCatching { api.deleteGrupo(id) }.isSuccess
        if (ok) refresh() else save(current().filter { it.id != id })
    }

    suspend fun aceptar(id: String) {
        val ok = runCatching { api.grupoAccion(id, GrupoActionRequest("aceptar")) }.isSuccess
        if (ok) refresh() else save(current().map { if (it.id == id) it.copy(estado = "Aceptado") else it })
    }

    suspend fun denegar(id: String) {
        val ok = runCatching { api.grupoAccion(id, GrupoActionRequest("denegar")) }.isSuccess
        if (ok) refresh() else save(current().map { if (it.id == id) it.copy(estado = "Denegado") else it })
    }

    private suspend fun current(): List<GrupoEspecial> {
        val prefs = context.dataStoreGrupos.data.first()
        val s = prefs[key]
        return if (s.isNullOrBlank()) emptyList() else decode(s)
    }

    private suspend fun save(list: List<GrupoEspecial>) {
        context.dataStoreGrupos.edit { p -> p[key] = encode(list) }
    }

    private fun decode(s: String): List<GrupoEspecial> = runCatching {
        json.decodeFromString(ListSerializer(GrupoEspecial.serializer()), s)
    }.getOrDefault(emptyList())

    private fun encode(list: List<GrupoEspecial>): String =
        json.encodeToString(ListSerializer(GrupoEspecial.serializer()), list)
}
