apply(from = rootProject.file("gradle/publish_jitpack.gradle.kts"))
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    // alias(libs.plugins.dagger.hilt.android)
    kotlin("kapt")
}

val jvmVersion = "11"

android {
    namespace = "com.eitanliu.starter"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.toVersion(jvmVersion)
        targetCompatibility = JavaVersion.toVersion(jvmVersion)
    }

    kotlinOptions {
        jvmTarget = jvmVersion
    }

    viewBinding { enable = true }
    dataBinding { enable = true }
}

dependencies {

    api(project(":utils"))
    api(project(":binding"))

    ////// test //////
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // implementation(libs.dagger.hilt.android)
    // kapt(libs.dagger.hilt.android.compiler)

    ////// framework //////
    implementation(libs.mmkv)
    implementation(libs.gson)
}