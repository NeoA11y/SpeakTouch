/*
 * app module build configurations.
 *
 * Copyright (C) 2023 Irineu A. Silva.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val versionMajor = 0 // 0..Infinity
val versionMinor = 1 // 0..9
val versionPatch = 2 // 0..9

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "com.neo.speaktouch"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    signingConfigs {
        create("release") {
            loadProperties("keystore.properties") { properties ->
                storeFile = rootProject.file(properties["storeFile"] as String)
                storePassword = properties["storePassword"] as String
                keyAlias = properties["keyAlias"] as String
                keyPassword = properties["keyPassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "com.neo.speaktouch"

        minSdk = 22
        targetSdk = 33

        versionCode = getVersionCode()
        versionName = getVersionName()
        resourceConfigurations.addAll(
            listOf(
                "en",
                "pl",
                "pt"
            )
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = false

            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.timber)

    // Unit test
    testImplementation(libs.junit)

    // Instrumented test
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}

// tasks
tasks.register("versionCode") {
    doLast {
        println("versionCode is ${getVersionCode()} from v${getVersionName()}")
    }
}

// functions
fun loadProperties(
    fileName: String,
    postResult: (Properties) -> Unit
) = postResult(
    Properties().apply {
        load(FileInputStream(rootProject.file(fileName)))
    }
)

fun getVersionCode() = versionMajor * 100 + versionMinor * 10 + versionPatch
fun getVersionName() = "$versionMajor.$versionMinor.$versionPatch"
