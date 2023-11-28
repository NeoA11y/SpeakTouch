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
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val useKeystoreProperties = keystorePropertiesFile.canRead()

java {
    toolchain {
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

    if (useKeystoreProperties) {
        signingConfigs {
            create("release") {
                properties(name = "keystore.properties") { properties ->
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

        minSdk = 22
        targetSdk = 34

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
        release {
            isMinifyEnabled = false

            if (useKeystoreProperties) {
                signingConfig = signingConfigs.getByName("release")
            }
        }

        debug {
            isMinifyEnabled = false

            applicationIdSuffix = ".debug"

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = "${rootProject.name}-${buildType.name}.apk"
                if (buildType.name == "release" && !useKeystoreProperties) {
                    output.outputFileName = output.outputFileName.replace(".apk", "-unsigned.apk")
                }
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
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

    // Instrumented test
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}
