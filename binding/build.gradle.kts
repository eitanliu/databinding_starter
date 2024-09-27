plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    `maven-publish`
}

fun properties(key: String) = project.findProperty(key).toString()
fun systemEnv(key: String): String? = System.getenv(key)
println("System.env $project ===> ${System.getenv()}")
val jitpack = (systemEnv("JITPACK") ?: "false").toBoolean()

afterEvaluate {

    publishing {
        //配置maven仓库
        repositories {
            maven {
                if (!jitpack) url = uri("${layout.buildDirectory.file("repo").get()}")
            }
        }

        publications {
            create<MavenPublication>("product") {
                from(components["release"])
                // artifact(sourcesJar)

                if (jitpack) {
                    groupId = arrayOf(systemEnv("GROUP"), systemEnv("ARTIFACT")).joinToString(".")
                    version = systemEnv("VERSION")
                }
                println("Publication $project ===> $groupId:$artifactId:$version")
            }

        }
    }
}

android {
    namespace = "com.eitanliu.binding"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding { enable = true }
    dataBinding { enable = true }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    implementation(libs.glide)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}