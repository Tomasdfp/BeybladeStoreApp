
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // KSP removed (no longer using Room)
}

android {
    namespace = "com.proyecto.BeybladeStoreApp"
    compileSdk = 36

    // Note: sourceSets exclusion removed — legacy/duplicate files should be deleted or
    // replaced with wrapper-forwarders. Excluding Java sources via this block caused
    // a Kotlin DSL parse error in earlier edits.

    defaultConfig {
    applicationId = "com.proyecto.BeybladeStoreApp"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Material components for Android (required for Theme.Material3.* styles)
    implementation("com.google.android.material:material:1.9.0")

    // Room removed: using DataStore for persistence
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.foundation)
    // Material icons (extended set) used by cart UI
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    // KSP room-compiler removed
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    // Networking libraries (Retrofit + OkHttp + Coroutines)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.compose.ui:ui-text")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.3")
    // DataStore preferences
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    // Gson (ensure available for DataStore JSON serialization)
    implementation("com.google.code.gson:gson:2.10.1")
    // Coil for image loading (used to show admin-uploaded images)
    implementation("io.coil-kt:coil-compose:2.4.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
