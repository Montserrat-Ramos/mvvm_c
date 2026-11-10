package com.uniconet.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStoreAuth by preferencesDataStore(name = "uniconet_auth")

// Contraseñas locales por rol y estado de sesión (para biometría).
// Por defecto: Estudiante sebas@gmail.com / sebas, Maestro maestro@gmail.com / maestro123
class AuthRepository(private val context: Context) {

    private val keyEstudiante = stringPreferencesKey("pass_estudiante")
    private val keyMaestro = stringPreferencesKey("pass_maestro")
    private val keySesionEstudiante = booleanPreferencesKey("sesion_estudiante")
    private val keySesionMaestro = booleanPreferencesKey("sesion_maestro")

    val passEstudiante: Flow<String> = context.dataStoreAuth.data.map { it[keyEstudiante] ?: "sebas" }
    val passMaestro: Flow<String> = context.dataStoreAuth.data.map { it[keyMaestro] ?: "maestro123" }
    val sesionEstudianteActiva: Flow<Boolean> = context.dataStoreAuth.data.map { it[keySesionEstudiante] ?: false }
    val sesionMaestroActiva: Flow<Boolean> = context.dataStoreAuth.data.map { it[keySesionMaestro] ?: false }

    suspend fun currentPassEstudiante(): String = context.dataStoreAuth.data.first()[keyEstudiante] ?: "sebas"
    suspend fun currentPassMaestro(): String = context.dataStoreAuth.data.first()[keyMaestro] ?: "maestro123"

    suspend fun setPassEstudiante(nueva: String) {
        context.dataStoreAuth.edit { it[keyEstudiante] = nueva }
    }

    suspend fun setPassMaestro(nueva: String) {
        context.dataStoreAuth.edit { it[keyMaestro] = nueva }
    }

    suspend fun setSesionEstudiante(activa: Boolean) {
        context.dataStoreAuth.edit { it[keySesionEstudiante] = activa }
    }

    suspend fun setSesionMaestro(activa: Boolean) {
        context.dataStoreAuth.edit { it[keySesionMaestro] = activa }
    }

    suspend fun cerrarSesionEstudiante() = setSesionEstudiante(false)
    suspend fun cerrarSesionMaestro() = setSesionMaestro(false)
}
