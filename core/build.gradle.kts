import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.moviedb.core"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            FileInputStream(localPropertiesFile).use { input ->
                localProperties.load(input)
            }
        } else {
            logger.warn("local.properties file not found. Ensure it exists in the project root.")
        }

        val baseUrl = localProperties.getProperty("TMDB_BASE_URL")
        val baseUrlImage = localProperties.getProperty("TMDB_BASE_URL_IMAGE")
        val tmdbAuth = localProperties.getProperty("TMDB_AUTH")

        buildConfigField("String", "TMDB_BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "TMDB_BASE_URL_IMAGE", "\"$baseUrlImage\"")
        buildConfigField("String", "TMDB_AUTH", "\"$tmdbAuth\"")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.koin)
    implementation(libs.paging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}