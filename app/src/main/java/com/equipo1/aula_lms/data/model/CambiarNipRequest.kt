package com.equipo1.aula_lms.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CambiarNipRequest(
    @SerialName("nip_actual")
    val nipActual: String,

    @SerialName("nip_nuevo")
    val nipNuevo: String
)