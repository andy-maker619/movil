package com.equipo1.aula_lms.ui.inicio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equipo1.aula_lms.data.model.Usuario
import com.equipo1.aula_lms.ui.inicio.PantallaInicioBase

@Composable
fun InicioAlumno(
    usuario: Usuario,
    onCambiarNip: () -> Unit,
    onSesionCerrada: () -> Unit,
    viewModel: SesionViewModel = viewModel(
        factory = SesionViewModel.Factory
    )
) {
    val sesionCerrada by viewModel.sesionCerrada.collectAsState()

    LaunchedEffect(sesionCerrada) {
        if (sesionCerrada) {
            onSesionCerrada()
        }
    }

    PantallaInicioBase(
        titulo = "Inicio",
        tipoUsuario = "Alumno",
        usuario = usuario,
        onCambiarNip = onCambiarNip,
        onSesionCerrada = viewModel::cerrarSesion
    )

}