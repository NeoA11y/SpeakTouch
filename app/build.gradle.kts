/*
 * App build configurations.
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

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

appVersion(VersionConfig.Type.DEV) {
    major = 1
    minor = 0
    patch = 0
}

android {
    namespace = "com.neo.speaktouch"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    if (keystorePropertiesFile.canRead()) {
        signingConfigs {
            create("release") {
                properties(keystorePropertiesFile) { properties ->
                    storeFile = rootProject.file(properties.getProperty("storeFile"))
                    storePassword = properties.getProperty("storePassword")
                    keyAlias = properties.getProperty("keyAlias")
                    keyPassword = properties.getProperty("keyPassword")
                }
            }
        }
    }

    defaultConfig {
        applicationId = "com.neo.speaktouch"

        configure<BasePluginExtension> { archivesName.set(rootProject.name) }

        minSdk = 22
        targetSdk = 34

        resourceConfigurations.addAll(
            listOf(
                "en",
                "pl",
                "pt"
            )
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            if (keystorePropertiesFile.canRead()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }

        debug {
            isMinifyEnabled = false

            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Speak Touch - debug")

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    // Android X
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Material
    implementation(libs.material)

    // Log
    implementation(libs.timber)

    // Dagger Hilt
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Unit test
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}
