package com.equipo1.aula_lms.ui.inicio
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo1.aula_lms.R
import com.equipo1.aula_lms.data.model.Usuario
import com.equipo1.aula_lms.ui.inicio.InicioAlumno
import com.equipo1.aula_lms.ui.inicio.InicioDocente

private val AzulUAS = Color(0xFF022258)
private val DoradoUAS = Color(0xFFCF9506)

/**
 * Pantalla provisional de inicio de docentes y alumnos (HU-01).
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioBase(
    titulo: String,
    tipoUsuario: String,
    usuario: Usuario,
    onCambiarNip: () -> Unit,
    onSesionCerrada: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titulo,
                        color = AzulUAS,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    MenuDerecho(
                        onCambiarNip = onCambiarNip,
                        onSesionCerrada = onSesionCerrada
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Encabezado de bienvenida
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.logo_aula_uas
                    ),
                    contentDescription = "Logo de Aula UAS",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "¡Bienvenido!",
                        fontSize = 26.sp,
                        color = AzulUAS,
                        fontWeight = FontWeight.Bold
                    )


                    Text(
                        text = tipoUsuario,
                        color = DoradoUAS,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Tarjeta con información del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color.LightGray
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del usuario",
                        style = MaterialTheme.typography.titleMedium,
                        color = AzulUAS,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Nombre completo",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Text(
                        text = usuario.nombre,
                        color = AzulUAS,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Número de cuenta",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Text(
                        text = usuario.numeroCuenta,
                        color = AzulUAS,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Correo electrónico",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Text(
                        text = usuario.correo ?: "No registrado",
                        color = AzulUAS,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

}

@Composable
private fun MenuDerecho(
    onCambiarNip: () -> Unit,
    onSesionCerrada: () -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expandido = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Abrir menú",
                tint = AzulUAS
            )
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = {
                expandido = false
            }
        ) {
            DropdownMenuItem(
                text = { Text("Cambiar NIP") },
                onClick = {
                    expandido = false
                    onCambiarNip()
                }
            )
            DropdownMenuItem(
                text = { Text("Cerrar sesión") },
                onClick = {
                    expandido = false
                    onSesionCerrada()
                }
            )
        }
    }

}