// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2") // Ensure you have the latest AGP version
        classpath("com.google.gms:google-services:4.4.2")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
