# Uniconet (ProjecMovil)

Aplicación Android (Jetpack Compose) para la gestión académica de **Estudiantes** y **Maestros**: biblioteca/libros, foro de anuncios, grupos especiales y ajustes de cuenta. Incluye consumo de API REST en AWS, persistencia local y integraciones de cámara/galería, autenticación biométrica y visualización de PDF.

## Características principales

- **Roles:** Estudiante y Maestro con flujos y navegación diferenciados.
- **Biblioteca:** listado de libros, subida (maestro) y descarga/visualización (estudiante).
- **Foro:** publicación de anuncios por el maestro, comentarios y archivos adjuntos.
- **Grupos especiales:** creación de solicitudes (estudiante) y gestión/aceptación o rechazo (maestro).
- **Ajustes:** notificaciones, cambio de contraseña y cierre de sesión.
- **API en AWS:** sincronización de libros, anuncios y grupos con backend FastAPI + MySQL (`http://52.7.137.127:8080/api/`).
- **Persistencia local:** caché y datos locales con DataStore (sesión, preferencias y contenido), con recuperación tras reiniciar la app.

## Integraciones implementadas

1. **Cámara / Galería (Maestro, Foro):** al crear/publicar un anuncio, el selector de adjuntos permite tomar foto con la cámara, elegir imagen de la galería o seleccionar archivos (PDF/DOCX, etc.). Usa `ActivityResultContracts`, permisos de cámara y `FileProvider`.
2. **Autenticación biométrica:** si la sesión del rol (estudiante/maestro) ya estaba iniciada, al volver a la pantalla de acceso se solicita huella/biometría para continuar (`androidx.biometric`). Si el dispositivo no tiene biometría disponible/enrolada, se permite continuar con la sesión activa.
3. **PDF / Visor de archivos:** visualización integrada de archivos adjuntos (previsualización de PDF con `PdfRenderer`, imágenes y apertura con visor del sistema cuando aplica) en flujos reales de biblioteca/foro.

## Arquitectura (MVVM)

Separación por capas:

- **UI (`app/src/main/java/com/uniconet/app/presentation/...`, `ui/`, `navigation/`)**: pantallas en Jetpack Compose que observan estado expuesto por ViewModels (`StateFlow`/`collectAsState`) y disparan acciones.
- **ViewModel (`...ViewModel.kt`)**: coordina estado de UI y casos de uso, expone flujos y maneja eventos de pantalla.
- **Repositorio / Datos (`data/`)**: acceso a datos locales (DataStore) y sincronización con API cuando corresponde (`BookRepository`, `ForoRepository`, `GrupoRepository`, `AuthRepository`).
- **Red (`network/`)**: cliente Retrofit/OkHttp, configuración (`ApiConfig`, `NetworkModule`) e interfaz de endpoints (`UniconetApi`, DTOs).

**Flujo de datos (ejemplo):** `API (FastAPI) → Repository → ViewModel (StateFlow) → UI (Compose)`.

## Stack técnico

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose, Material 3, Navigation Compose
- **Arquitectura/estado:** ViewModel, StateFlow, coroutines
- **Red:** Retrofit, OkHttp (logging), kotlinx.serialization
- **Persistencia:** DataStore (Preferences)
- **Biometría:** androidx.biometric
- **Build:** Gradle (Kotlin DSL), AGP
- **SDK:** `compileSdk` 37, `targetSdk` 37, `minSdk` 24

## Backend (API)

- **Base URL:** `http://52.7.137.127:8080/api/` (configurada en `network/ApiConfig.kt`)
- **Servidor:** FastAPI + MySQL en AWS EC2 (ver `server/README.md` para despliegue y esquema).
- **Endpoints principales:** auth, libros (`/books`), foro/anuncios y comentarios (`/foro/anuncios`), grupos (`/grupos`), salud (`/health`), uploads.

> Nota: el login/cambio de contraseña en la app funciona actualmente con validación local; la sincronización remota está implementada principalmente para biblioteca, foro y grupos.

## Requisitos

- Android Studio (versión reciente recomendada)
- JDK 11+ (según configuración del proyecto)
- Android SDK (plataformas/herramientas compatibles con `compileSdk 37`)
- Dispositivo o emulador Android (API 24+)
- Conexión a internet para consumir la API

## Configuración y ejecución

1. Clona/abre el proyecto `ProjecMovil` en Android Studio.
2. Verifica `local.properties`/SDK y sincroniza Gradle (**Sync Project with Gradle Files**).
3. (Opcional) Ajusta la URL del backend en `app/src/main/java/com/uniconet/app/network/ApiConfig.kt` si cambia la IP/dominio.
4. Selecciona un dispositivo/emulador y ejecuta **Run > Run 'app'**.

### Compilar desde terminal

```bash
cd /ruta/a/ProjecMovil
export ANDROID_HOME=~/Android/Sdk
./gradlew :app:assembleDebug
```

APK generado:
- `app/build/outputs/apk/debug/app-debug.apk`

Instalación por ADB:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Permisos relevantes

- `INTERNET`, `ACCESS_NETWORK_STATE`: consumo de API.
- `CAMERA` (+ `uses-feature` opcional): captura de fotos para adjuntos en foro.
- `USE_BIOMETRIC`: autenticación biométrica.

## Persistencia local

Se utiliza **DataStore** para:
- estado de sesión por rol (biometría),
- credenciales/configuración local de acceso,
- caché y datos locales de libros, foro y grupos,
- estado de descargas/adjuntos según flujo.

Esto permite conservar información relevante entre reinicios de la aplicación.

## Estructura relevante

```
app/src/main/java/com/uniconet/app/
  data/          # Repositorios, modelos, DataStore, helpers
  network/       # ApiConfig, Retrofit, UniconetApi, DTOs
  navigation/    # NavHost y rutas
  presentation/  # Pantallas y ViewModels por módulo/rol
  ui/            # Tema y componentes UI
server/          # Backend FastAPI + MySQL (schema, README)
```

## Notas y limitaciones conocidas

- Algunos flujos aún son locales o parciales frente a la API (p. ej. login con validación local, likes/listado completo de comentarios, subida de archivos al endpoint de uploads).
- Las listas pueden no mostrar estados explícitos de carga/error/sin conexión en todas las pantallas; ante fallos de red se usan caches/fallbacks locales.
- Para biometría en emulador, configura una huella en el sistema del emulador.

## Evidencias / entregables

- Código fuente versionado (Git).
- APK instalable (debug): `app/build/outputs/apk/debug/app-debug.apk`.
- Documentación: este `README.md` y `server/README.md`.
- Evidencias visuales: capturas/video del flujo (agregar según entrega).
