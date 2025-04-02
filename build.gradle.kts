// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.javapoet)
    }

}

println("System.env ===> ${System.getenv()}")
println("System.props ===> ${System.getProperties()}")

allprojects {
    println("Project.props $project ===> ${project.properties}")
    configurations.all {
        resolutionStrategy {
            force(libs.okio)
            force(libs.okhttp)
            force(libs.androidx.core)
            force(libs.androidx.core.ktx)
            force(libs.androidx.recyclerview)
            force(libs.kotlin.stdlib)
            force(libs.kotlin.stdlib.jdk7)
            force(libs.kotlin.stdlib.jdk8)
            force(libs.kotlinx.coroutines.core)
            // force(libs.kotlinx.coroutines.core.jvm)
            // force(libs.kotlinx.coroutines.android)
        }
    }
}
