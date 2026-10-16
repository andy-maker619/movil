package com.equipo1.aula_lms.data

import android.util.Log
import com.equipo1.aula_lms.data.api.AuthApi
import com.equipo1.aula_lms.data.api.ErrorResponse
import com.equipo1.aula_lms.data.api.LoginRequest
import com.equipo1.aula_lms.data.api.json
import com.equipo1.aula_lms.data.model.CambiarNipRequest
import com.equipo1.aula_lms.data.model.Usuario
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val api: AuthApi,
    private val sesion: SessionStorage,
) {
    fun usuarioActual(): Usuario? = sesion.usuario()



    suspend fun login(numeroCuenta: String, nip: String): Result<Usuario> =
        try {
            val respuesta = api.login(LoginRequest(numeroCuenta, nip))
            sesion.guardar(respuesta.token, respuesta.usuario)
            Result.success(respuesta.usuario)
        } catch (e: HttpException) {
            Result.failure(Exception(mensajeDeError(e)))
        } catch (e: IOException) {
            Log.w(TAG, "Error de red al iniciar sesión", e)
            Result.failure(Exception("No se pudo conectar con el servidor. Intenta de nuevo más tarde."))
        }

    suspend fun logout(): Result<Unit> {
        val resultado = runCatching { api.logout() }
        sesion.borrar() //
        return resultado.map { }
    }
    // Muestra el mensaje que manda la API (credenciales incorrectas, cuenta desactivada, etc.)
    private fun mensajeDeError(e: HttpException): String {
        val cuerpo = e.response()?.errorBody()?.string()
        val mensaje = cuerpo?.let {
            runCatching { json.decodeFromString(ErrorResponse.serializer(), it).message }.getOrNull()
        }
        return mensaje ?: "Error al iniciar sesión (HTTP ${e.code()})."
    }

    suspend fun verificarSesion(): Result<Usuario> {
        val token = sesion.token()
            ?: return Result.failure(
                Exception("No hay una sesión guardada")
            )

        return try {
            val usuario = api.me()

            sesion.guardar(token, usuario)

            Result.success(usuario)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sesion.borrar()
            }

            Result.failure(
                Exception("No se pudo verificar la sesión")
            )
        } catch (e: IOException) {
            Result.failure(
                Exception("No se pudo conectar con el servidor")
            )
        }
    }

    suspend fun cambiarNip(
        nipActual: String,
        nipNuevo: String
    ): Result<Unit> {
        return try {
            val respuesta = api.cambiarNip(
                CambiarNipRequest(
                    nipActual = nipActual,
                    nipNuevo = nipNuevo
                )
            )

            if (respuesta.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(
                        when (respuesta.code()) {
                            401 -> "Tu sesión expiró. Inicia sesión nuevamente."
                            422 -> "El NIP actual no es válido o los datos son incorrectos."
                            else -> "No se pudo cambiar el NIP."
                        }
                    )
                )
            }
        } catch (e: java.io.IOException) {
            Result.failure(
                Exception("No se pudo conectar con el servidor.")
            )
        } catch (e: retrofit2.HttpException) {
            Result.failure(
                Exception("Error de comunicación con el servidor.")
            )
        }
    }

    private companion object {
        const val TAG = "AuthRepository"
    }
}

