// ─────────────────────────────────────────────────────────────
//  VELOCITÀ ELITE — build.gradle.kts  (app module)
//
//  All library versions align with a stable BOM release.
//  Using Kotlin DSL syntax throughout.
// ─────────────────────────────────────────────────────────────


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace         = "com.velocita.elite"
    compileSdk        = 35

    defaultConfig {
        applicationId = "com.velocita.elite"
        minSdk        = 26          // Ensures broad device support (API 26+)
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        compose = true  // Enable Jetpack Compose
    }
}

dependencies {

    // ── Compose BOM ───────────────────────────────────────────
    // Pins all Compose library versions to a tested-together set.
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // ── Core Compose ──────────────────────────────────────────
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ── Material 3 ────────────────────────────────────────────
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended) // Full icon set

    // ── Navigation ────────────────────────────────────────────
    implementation(libs.androidx.navigation.compose)

    // ── ViewModel ─────────────────────────────────────────────
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ── Activity ──────────────────────────────────────────────
    implementation(libs.androidx.activity.compose)

    // ── Core KTX ──────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)

    // ── Image Loading ─────────────────────────────────────────
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // ── Testing ───────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
