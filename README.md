 # Beyblade Store (Android)

 Clean, small Android store app built with Jetpack Compose and MVVM. This repository contains the Android application (module `app`) for a simple Beyblade store demo used for teaching and assessment.

 Key facts
 - ApplicationId / namespace: `com.proyecto.BeybladeStoreApp`
 - Default admin credentials (for local testing): `admin` / `123456`
 - UI: Jetpack Compose (Material3)
 - Architecture: MVVM (Repository -> ViewModel -> Composable UI)
 - Persistence: Jetpack DataStore (preferences) for lightweight session/users storage

 Features (rubric-aligned)
 - Animated splash screen shown on first open
 - User registration and login backed by DataStore
 - Logout that clears the session from DataStore
 - Product catalog (8–10 seeded products)
 - Product detail screen reachable via navigation route `detalle/{id}` (required alias)
 - Admin sections (add/edit products, manage users/orders)
 - Cart and orders screens

 Table of contents
 - Overview
 - How to build & run (Windows / PowerShell)
 - App flows to test
 - Project structure & architecture notes
 - Debugging & crash logs
 - Tests and quality checks
 - Contributing

 Overview
 --------
 This app is a compact educational/demo store implemented in Kotlin using Jetpack Compose. It follows a straightforward MVVM approach with repositories that encapsulate data access and ViewModels that expose state to composables. The UI uses Navigation Compose and includes the required `detalle/{id}` route for product details.

 How to build & run (Windows / PowerShell)
 ----------------------------------------
 1) Open PowerShell and change to the project root (where the top-level `build.gradle.kts` is):

 ```powershell
 cd 'C:\Users\urtub\OneDrive\Escritorio\BeyStore\BeybladeStoreApp'
 ```

 2) Build the debug APK:

 ```powershell
 .\gradlew.bat clean :app:assembleDebug
 ```

 3) Install the debug APK on a connected device or emulator (adb required):

 ```powershell
 # path to the generated debug APK
 $apk = '.\app\build\outputs\apk\debug\app-debug.apk'
 adb install -r $apk
 ```

 4) Launch the app from the device or via adb:

 ```powershell
 # open the launcher activity (replace package if you customized it)
 adb shell monkey -p com.proyecto.BeybladeStoreApp -c android.intent.category.LAUNCHER 1
 ```

 App flows to test (smoke checklist)
 ----------------------------------
 - On first launch: animated splash appears and leads to Login screen
 - Register a new user (Register screen) — verifies DataStore writes
 - Login with the registered user and validate navigation to Home
 - View product catalog (8–10 items) and open a product detail via `detalle/{id}`
 - Add a product to cart and view Cart
 - Use logout: session is cleared and app returns to Login
 - (Optional) Log in as `admin` / `123456` to access admin sections

 Project structure (high level)
 -------------------------------
 - app/
	 - src/main/java/com/proyecto/BeybladeStoreApp/
		 - ui/theme/screen/  -> Composable screens (Login, Register, Home, ProductList, ProductDetail, Admin screens, etc.)
		 - repository/       -> Repositories (AuthRepository, ProductRepository, CartRepository)
		 - data/models/      -> Data classes (Product, CartItem, User)
		 - viewModel/        -> ViewModel classes (AuthViewModel, ProductsViewModel, CartViewModel)
	 - build.gradle.kts

 Architecture notes
 ------------------
 - Repositories encapsulate data access (in-memory seeds + DataStore persistence where appropriate).
 - ViewModels expose Flow/StateFlow that UI collects with `collectAsState()`.
 - Navigation uses Navigation Compose. The product detail route uses the alias `detalle/{id}` (so you can call `navController.navigate("detalle/3")`).

 Debugging & crash logs
 ----------------------
 The app includes lightweight crash logging to a file inside the app's files directory to help debug early-start crashes.
 - startup marker: `startup_log.txt`
 - uncaught-exception dump: `crash_report.txt`

 To pull these files from a device/emulator using adb (PowerShell):

 ```powershell
 # Replace package with the app id if changed
 $pkg = 'com.proyecto.BeybladeStoreApp'

 # Try to cat the file (requires 'run-as' and that the device allows it)
 adb shell run-as $pkg cat files/crash_report.txt

 # Or pull the files to your machine
 adb shell run-as $pkg ls -l files
 adb shell run-as $pkg cat files/startup_log.txt > startup_log.txt
 adb shell run-as $pkg cat files/crash_report.txt > crash_report.txt
 ```

 If `run-as` fails on some devices/emulators, you can use `adb pull` when the device is rooted, or read logs with `adb logcat`:

 ```powershell
 adb logcat -d | Out-File -FilePath app_logcat.txt -Encoding utf8
 ```

 Known developer notes
 ---------------------
 - Default admin credentials: `admin` / `123456` (only for local/dev testing)
 - DataStore is used for sessions and small persisted lists; reads are done inside coroutines (safely) but be mindful when using `.first()` on large flows.

 Tests & quality
 ---------------
 - The project is focused on UI + architecture; there are currently no automated instrumentation/unit tests included by default. Adding a minimal unit test for ViewModel behavior is a recommended next step.

 Contributing
 ------------
 1. Fork the repo and create a feature branch.
 2. Keep changes small and focused (UI, tests, or repository improvements).
 3. If adding libraries, prefer well-known, actively maintained ones and add them to the Gradle files.

 License & contact
 -----------------
 This demo contains example code for learning and evaluation. If you want me to prepare a PR with additional fixes or CI, tell me which branch and I'll prepare it.

 ----
 If you want any wording changes, additional screenshots, or a quick CONTRIBUTING.md + tiny unit test, tell me which you prefer and I will add them.