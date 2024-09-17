plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    kotlin("kapt")
}

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    ////// jetpack //////
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
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.work.multiprocess)

    ////// test //////
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    var isOutMode = false
    if (isOutMode) {
        implementation(platform(libs.firebase.bom))
        implementation(libs.firebase.analytics.ktx)
        implementation(libs.firebase.config.ktx)
        implementation(libs.firebase.crashlytics)
        implementation(libs.firebase.messaging)
        implementation(libs.firebase.perf)
        implementation(libs.play.services.base)
        implementation(libs.play.services.location)
        implementation(libs.play.services.auth)
        implementation(libs.play.review)
        implementation(libs.play.review.ktx)
        implementation(libs.google.ump)
        implementation(libs.google.scanning)
        implementation(libs.billing)
    } else {
        // implementation(project(":mo"))
        implementation(libs.google.ump) {
            exclude(libs.play.services.basement.getModuleMap())
        }
        implementation(libs.billing) {
            exclude(libs.play.services.base.getModuleMap())
            exclude(libs.play.services.basement.getModuleMap())
            exclude(libs.play.services.tasks.getModuleMap())
            exclude(libs.play.services.location.getModuleMap())
        }
    }

    ////// framework //////
    implementation(libs.mmkv)
    implementation(libs.gson)
    implementation(libs.lottie)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
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
