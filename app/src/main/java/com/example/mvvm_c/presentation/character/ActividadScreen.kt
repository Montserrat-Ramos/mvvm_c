package com.example.mvvm_c.presentation.character

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActividadScreen() {
    var textoActividad by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. TÍTULO
        Text(
            text = "Actividad Versión 2",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TEXTO CON TU NOMBRE Y MATRÍCULA
        Text(
            text = "Brisa Rosario Nuñez Cruz | Matrícula: 253421",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. ICONO O IMAGEN
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Icono Destacado",
            modifier = Modifier.size(90.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. CAMPO DE TEXTO
        OutlinedTextField(
            value = textoActividad,
            onValueChange = { textoActividad = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Escribe el contenido aquí...") },
            shape = RoundedCornerShape(size = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. BOTÓN PRINCIPAL (Modificado con un height de 56.dp para que sea más grande)
        Button(
            onClick = { /* Acción del botón */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Se aumentó la altura del botón
            shape = RoundedCornerShape(size = 16.dp)
        ) {
            Text(
                text = "Enviar Formulario",
                fontSize = 18.sp, // Texto ligeramente más grande para el botón
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}