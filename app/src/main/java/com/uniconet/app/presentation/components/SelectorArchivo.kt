package com.uniconet.app.presentation.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.uniconet.app.data.MaestroArchivoHelper
import com.uniconet.app.ui.theme.AzulAccionUni
import com.uniconet.app.ui.theme.BordeCristal
import com.uniconet.app.ui.theme.CristalNegro
import com.uniconet.app.ui.theme.TextoSecundario
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Selector de archivo para Maestro (Foro y Biblioteca).
// Permite elegir desde Cámara, Galería o Archivos del dispositivo.
@Composable
fun SelectorArchivo(
    titulo: String,
    subtitulo: String,
    archivoUri: Uri?,
    nombreArchivo: String?,
    onArchivoElegido: (Uri?, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mostrarOpciones by remember { mutableStateOf(false) }
    var camaraUri by remember { mutableStateOf<Uri?>(null) }
    var camaraNombre by remember { mutableStateOf("") }

    val pickerArchivos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val nombre = MaestroArchivoHelper.nombreVisible(context, uri)
        onArchivoElegido(uri, nombre)
    }

    val pickerGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val nombre = MaestroArchivoHelper.nombreVisible(context, uri)
        onArchivoElegido(uri, nombre)
    }

    val tomarFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok: Boolean ->
        if (ok && camaraUri != null) {
            onArchivoElegido(camaraUri, camaraNombre.ifBlank { "foto.jpg" })
        } else {
            onArchivoElegido(null, "")
        }
    }

    val pedirPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val (uri, nombre) = crearUriFoto(context)
            camaraUri = uri
            camaraNombre = nombre
            tomarFoto.launch(uri)
        }
    }

    fun abrirCamara() {
        val permiso = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED) {
            val (uri, nombre) = crearUriFoto(context)
            camaraUri = uri
            camaraNombre = nombre
            tomarFoto.launch(uri)
        } else {
            pedirPermisoCamara.launch(permiso)
        }
    }

    if (mostrarOpciones) {
        AlertDialog(
            onDismissRequest = { mostrarOpciones = false },
            containerColor = Color(0xFF1E1E24),
            title = { Text("Adjuntar archivo", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OpcionAdjunto(
                        icono = { Icon(Icons.Default.CameraAlt, null, tint = Color.White) },
                        texto = "Cámara",
                        subtitulo = "Tomar una foto"
                    ) {
                        mostrarOpciones = false
                        abrirCamara()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OpcionAdjunto(
                        icono = { Icon(Icons.Default.PhotoLibrary, null, tint = Color.White) },
                        texto = "Galería",
                        subtitulo = "Elegir una imagen"
                    ) {
                        mostrarOpciones = false
                        pickerGaleria.launch("image/*")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OpcionAdjunto(
                        icono = { Icon(Icons.Default.Folder, null, tint = Color.White) },
                        texto = "Archivos",
                        subtitulo = "PDF, DOCX u otros"
                    ) {
                        mostrarOpciones = false
                        pickerArchivos.launch("*/*")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarOpciones = false }) {
                    Text("Cancelar", color = AzulAccionUni)
                }
            }
        )
    }

    Surface(
        onClick = { if (archivoUri == null) mostrarOpciones = true },
        shape = RoundedCornerShape(16.dp),
        color = if (archivoUri != null) CristalNegro.copy(alpha = 0.85f) else CristalNegro,
        border = BorderStroke(
            1.dp,
            if (archivoUri != null) AzulAccionUni.copy(alpha = 0.6f) else BordeCristal
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (archivoUri != null) Icons.Default.CheckCircle else Icons.Default.AttachFile,
                contentDescription = null,
                tint = if (archivoUri != null) Color(0xFF4CAF50) else Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (archivoUri != null) nombreArchivo ?: "Archivo seleccionado" else titulo,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (archivoUri != null) "Archivo listo para subir" else "$subtitulo · Cámara o galería",
                    color = TextoSecundario,
                    fontSize = 11.sp
                )
            }
            if (archivoUri != null) {
                IconButton(onClick = { onArchivoElegido(null, "") }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Clear, contentDescription = "Quitar", tint = TextoSecundario, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun OpcionAdjunto(
    icono: @Composable () -> Unit,
    texto: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = CristalNegro,
        border = BorderStroke(1.dp, BordeCristal),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            icono()
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(texto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitulo, color = TextoSecundario, fontSize = 12.sp)
            }
        }
    }
}

private fun crearUriFoto(context: Context): Pair<Uri, String> {
    val dir = File(context.cacheDir, "camera").apply { mkdirs() }
    val nombre = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
    val archivo = File(dir, nombre)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
    return uri to nombre
}
