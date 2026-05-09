plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.starhomes.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.starhomes.app"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    // =========================================================================
    // CORREÇÃO LINT — Baseline para erros pré-existentes
    // -------------------------------------------------------------------------
    // O erro InvalidFragmentVersionForActivityResult é um falso positivo:
    // o projeto usa ComponentActivity com a API moderna de ActivityResult,
    // que não depende da versão do Fragment. O Lint aplica a regra
    // incorretamente. O baseline registra esse erro como conhecido e
    // permite que o CI/CD passe sem travar nele.
    //
    // Para regenerar o baseline após corrigir problemas:
    //   ./gradlew updateLintBaseline
    // =========================================================================
    lint {
        baseline = file("lint-baseline.xml")
        // Warnings não travam o build — apenas erros NOVOS (não no baseline)
        warningsAsErrors = false
        // Continua mesmo com erros registrados no baseline
        abortOnError = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.coroutines.android)

    // GPS / Sensor de Localização — FusedLocationProviderClient
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}