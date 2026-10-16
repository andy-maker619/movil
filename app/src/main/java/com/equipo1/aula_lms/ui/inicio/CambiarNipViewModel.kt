package com.equipo1.aula_lms.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.equipo1.aula_lms.AulaApp
import com.equipo1.aula_lms.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CambiarNipUiState(
    val nipActual: String = "",
    val nipNuevo: String = "",
    val confirmacion: String = "",
    val cargando: Boolean = false,
    val mensaje: String? = null,
    val exito: Boolean = false
)

class CambiarNipViewModel(
    private val repositorio: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(CambiarNipUiState())
    val estado = _estado.asStateFlow()

    fun actualizarNipActual(valor: String) {
        _estado.update {
            it.copy(nipActual = valor, mensaje = null, exito = false)
        }
    }

    fun actualizarNipNuevo(valor: String) {
        _estado.update {
            it.copy(nipNuevo = valor, mensaje = null, exito = false)
        }
    }

    fun actualizarConfirmacion(valor: String) {
        _estado.update {
            it.copy(confirmacion = valor, mensaje = null, exito = false)
        }
    }

    fun cambiarNip() {
        val datos = _estado.value

        if (datos.cargando) return

        val error = when {
            datos.nipActual.isBlank() ->
                "Ingresa tu NIP actual."

            datos.nipNuevo.isBlank() ->
                "Ingresa el nuevo NIP."

            datos.confirmacion.isBlank() ->
                "Confirma el nuevo NIP."

            datos.nipNuevo != datos.confirmacion ->
                "Los NIP nuevos no coinciden."

            datos.nipActual == datos.nipNuevo ->
                "El nuevo NIP debe ser diferente."

            else -> null
        }

        if (error != null) {
            _estado.update {
                it.copy(mensaje = error, exito = false)
            }
            return
        }

        viewModelScope.launch {
            _estado.update {
                it.copy(cargando = true, mensaje = null, exito = false)
            }

            repositorio.cambiarNip(
                nipActual = datos.nipActual,
                nipNuevo = datos.nipNuevo
            ).fold(
                onSuccess = {
                    _estado.value = CambiarNipUiState(
                        mensaje = "Tu NIP se cambió correctamente.",
                        exito = true
                    )
                },
                onFailure = { errorApi ->
                    _estado.update {
                        it.copy(
                            cargando = false,
                            mensaje = errorApi.message
                                ?: "No se pudo cambiar el NIP.",
                            exito = false
                        )
                    }
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = this[
                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                ] as AulaApp

                CambiarNipViewModel(aplicacion.authRepository)
            }
        }
    }
}