plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.dagger.hilt.android)
    kotlin("kapt")
}

val jvmVersion = "11"

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        // applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.toVersion(jvmVersion)
        targetCompatibility = JavaVersion.toVersion(jvmVersion)
    }

    kotlinOptions {
        jvmTarget = jvmVersion
    }

    viewBinding { enable = true }
    dataBinding { enable = true }

    buildFeatures {
        buildConfig = true
        // compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force(libs.okio)
        force(libs.okhttp)
        force(libs.androidx.core)
        force(libs.androidx.core.ktx)
        force(libs.androidx.recyclerview)
    }
}

dependencies {

    implementation(project(":binding"))
    implementation(project(":starter"))

    ////// test //////
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    ////// jetpack //////
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.work.multiprocess)

    ////// framework //////
    implementation(libs.mmkv)
    implementation(libs.gson)
    implementation(libs.lottie)
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.glide.transformations)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    ////// third party //////
    implementation(libs.xxPermissions)
    implementation(libs.blankjUtils)
    implementation(libs.binding.collection.adapter)
    implementation(libs.binding.collection.adapter.recyclerview)
    implementation(libs.binding.collection.adapter.viewpager2)
}

fun ProviderConvertible<MinimalExternalModuleDependency>.getModuleMap() =
    asProvider().getModuleMap()

fun Provider<MinimalExternalModuleDependency>.getModuleMap() = mapOf(
    "group" to get().module.group,
    "module" to get().module.name,
)
