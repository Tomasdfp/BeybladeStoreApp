# BeybladeStoreApp

## Descripción breve
 
 BeybladeStoreApp es una aplicación de tienda móvil (Android) para gestionar un catálogo de productos tipo "Beyblade". Implementa un flujo básico de e-commerce educativo: pantalla splash animada, autenticación (registro/login) con persistencia local mediante DataStore, catálogo de productos con detalle por id, carrito y roles simples (usuario / admin). La arquitectura sigue el patrón MVVM.

## Objetivos de la aplicación — Fase 1

- Implementar una pantalla Splash animada y navegación hacia la pantalla de login.
- Autenticación básica: registro y login con persistencia de sesión usando Jetpack DataStore (Preferences). Soporte de logout.
- Catálogo de productos (8–10 productos de ejemplo) y pantalla de detalle accesible por ruta `detalle/{id}`.
- Estructura MVVM: Repositorios → ViewModels → Composables (Jetpack Compose).
- Interfaz usable y estable (la app no debe cerrarse inesperadamente durante el flujo principal).

## Características y funcionalidades

- Splash animado con logo y botón para continuar.
- Registro y login de usuarios (local, DataStore). Sesión persistente.
- Logout que borra la sesión persistente.
- Catálogo navegable con lista de productos (grid/list). Cada elemento abre el detalle vía ruta con id.
- Detalle de producto con imágenes, descripción, precio y selector de cantidad.
- Carrito básico y flujo de pedidos (simulado/local).
- Panel de administración mínimo: añadir/editar productos desde la app (interfaz local).
- Persistencia local de catálogo, carrito y usuarios mediante DataStore.
- Cumple requisitos básicos de la rúbrica (animaciones, navegación, MVVM, persistencia).

## Requisitos del sistema

- Sistema operativo: Windows / macOS / Linux para desarrollo (Android SDK necesario para compilación y emulación).
- Java JDK 11+ (compatible con la versión de Gradle usada por el wrapper). Recomendado: OpenJDK 11 o 17.
- Android Studio (Arctic Fox / Bumblebee o superior) con Android SDK y emulador configurados.
- Espacio en disco suficiente y permisos para ejecutar Gradle wrapper.
- Gradle Wrapper incluido en el repositorio — no es necesario instalar Gradle globalmente.

## Cómo compilar y ejecutar (rápido)

Usando PowerShell en Windows (desde la raíz del proyecto `BeybladeStoreApp`):

```powershell
# Ensamblar APK de debug (más rápido, omite lint)
.\gradlew.bat :app:assembleDebug -x lint

# Crear APK release (firmado no incluido — requiere configuración de signingConfigs)
.\gradlew.bat :app:assembleRelease
```

Recomendación: abrir el proyecto en Android Studio y ejecutar en un emulador o dispositivo físico para depuración y pruebas de UI.

## Estructura del proyecto

Raíz del repositorio (resumen):

- BeybladeStoreApp/
	- app/                      -> Módulo Android con código fuente (UI, ViewModels, repositorios)
		- src/main/java/com/.../ui/theme/screen/  -> Composables / pantallas (Login, Register, Splash, ProductList, ProductDetail, etc.)
		- src/main/res/drawable/   -> Imágenes y logos (por ejemplo `pngegg.png` usado en Splash)
		- build.gradle.kts
	- build.gradle.kts
	- gradle/...
	- settings.gradle.kts
	- README.md                 -> Este archivo
	- scripts/                  -> Scripts de mantenimiento (ej: `remove_comments_and_clean.ps1`)


## Notas y consideraciones (mejoras posibles, limitaciones)

- Backups: se ejecutó un script de limpieza que eliminó comentarios de código; para cada archivo modificado se creó un backup con extensión `.bak` en el mismo directorio.
- Persistencia y sincronización con servidor: actualmente la app usa DataStore para persistencia local. Para producción se recomienda integrar un backend real (por ejemplo Xano/REST) y manejar sincronización/errores de red.
- Seguridad: las credenciales y la persistencia local son simples y educativas. Para una app real, no almacenar contraseñas en texto plano y añadir cifrado/validación robusta.
- Tests: hay pocos/ningunos tests automatizados en la rama actual; añadir unit/UI tests (Espresso/Compose Test) sería recomendable.
- Build: el proyecto usa Gradle wrapper; verifica la versión de JDK si hay errores de compilación en diferentes máquinas.
- Internacionalización: actualmente los textos están en español en la mayoría de pantallas; preparar `strings.xml` y recursos para múltiples idiomas si se requiere.

## Autores

- Tomás del Fierro
- Cristóbal Martínez

---

Si necesitas que deje preparada una rama y haga push al remoto, pásame la URL del repositorio remoto (o ejecuta la autenticación local con `gh auth login`) y con gusto lo subo. También puedo restaurar archivos desde los `.bak` si así lo decides.

 # Beyblade Store (Android)

 Clean, small Android store app built with Jetpack Compose and MVVM. This repository contains the Android application (module `app`) for a simple Beyblade store demo used for teaching and assessment.
Título del proyecto

BeybladeStoreApp

Descripción breve de la aplicación

BeybladeStoreApp es una aplicación Android de tienda que permite visualizar un catálogo de productos tipo Beyblade, autenticar usuarios, gestionar un carrito y simular pedidos. Utiliza arquitectura MVVM y persistencia local con Jetpack DataStore.

Objetivos de la aplicación Fase 1

- Implementar pantalla Splash animada y navegación hacia el login.
- Implementar registro y login con persistencia de sesión local (DataStore) y funcionalidad de logout.
- Proveer un catálogo de 8–10 productos de ejemplo y una pantalla de detalle accesible por ruta `detalle/{id}`.
- Aplicar arquitectura MVVM (Repositorios, ViewModels, Composables) y garantizar estabilidad en el flujo principal.

Características y Funcionalidades

- Splash animado con logo y botón para continuar.
- Registro y login de usuarios con sesión persistente.
- Logout que elimina la sesión almacenada.
- Catálogo navegable y pantalla de detalle por id.
- Carrito de compras básico y simulación de pedidos.
- Panel administrativo local para añadir y editar productos.

Requisitos del sistema

Requisitos mínimos para un equipo de desarrollo (PC):

- Sistema operativo: Windows 10/11, macOS 10.15+ o distribuciones Linux compatibles.
- CPU: procesador de 4 núcleos o superior.
- Memoria RAM: 8 GB (mínimo); 16 GB recomendado.
- Almacenamiento libre: 10 GB mínimo para SDKs y builds.
- Java JDK 11 o superior instalado.
- Android Studio y Android SDK con plataformas y herramientas instaladas.

Requisitos recomendados para ejecución en dispositivo Android:

- Versión de Android: Android 8.0 (API 26) o superior.
- Memoria: 2 GB RAM mínimo; 4 GB o más recomendado.
- Espacio libre: al menos 100 MB para la instalación y datos de la app.
- Habilitar depuración USB para instalación desde Android Studio o usar un emulador con imágenes del sistema.

Estructura del proyecto

- BeybladeStoreApp/
	- app/
		- src/main/java/.../ui/theme/screen/   (Composables y pantallas: Splash, Login, Register, ProductList, ProductDetail, etc.)
		- src/main/res/drawable/              (Recursos gráficos, p. ej. pngegg.png)
		- build.gradle.kts
	- settings.gradle.kts
	- build.gradle.kts
	- README.md

Notas/Consideraciones

- Se generaron backups con extensión `.bak` para los archivos modificados por procesos de limpieza; revisar estos archivos si se necesita recuperar comentarios o documentación previa.
- La persistencia actual es local mediante DataStore. Para uso en producción se recomienda integrar un backend y revisar la seguridad en el manejo de credenciales.
- Falta cobertura de pruebas automatizadas; se recomienda agregar tests unitarios y de UI y configurar signingConfigs para builds de release.

Autores

- Tomás del Fierro
- Cristóbal Martínez
