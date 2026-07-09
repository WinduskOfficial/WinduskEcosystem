// Code was made with Gemini
plugins {
    alias(libs.plugins.androidLibrary)
    id("kotlin-parcelize")
}

android {
    namespace = "com.windusk.lazybundle"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

ext["PUBLISH_ARTIFACT_ID"] = "lazybundle"
apply(from = "${rootDir}/scripts/publish-common.gradle")