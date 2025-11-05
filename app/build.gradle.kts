plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

android {
    namespace = "io.kitsuri.pelauncher"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.kitsuri.pelauncher"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
        }
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
    }

    // ✅ Modern Kotlin compiler DSL
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.7.0"
    }

    packaging {
        jniLibs.useLegacyPackaging = true
        resources.excludes.addAll(
            setOf("DebugProbesKt.bin")
        )
        resources.pickFirsts.addAll(
            setOf(
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES"
            )
        )
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":minecraft"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.apk.parser)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.conscrypt.android)
    implementation(libs.androidx.games.activity.v122)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}