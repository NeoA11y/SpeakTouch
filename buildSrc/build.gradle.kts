plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "4.1.0"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    implementation("com.android.tools.build:gradle:8.1.2")
}