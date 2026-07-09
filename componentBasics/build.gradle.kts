// Code was made with Gemini
plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.windusk.ecosystemBasics"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        aidl = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":basics"))
    implementation(project(":biteUi"))
    implementation(project(":ecosystem"))
    implementation(project(":lazyBundle"))
    implementation(project(":sharedFile"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

ext["PUBLISH_ARTIFACT_ID"] = "ecosystem-basics-component"
apply(from = "${rootDir}/scripts/publish-common.gradle")