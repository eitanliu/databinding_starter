# Databinding Starter

[![](https://jitpack.io/v/eitanliu/databinding_starter.svg)](https://jitpack.io/#eitanliu/databinding_starter)  

### How to

To get a Git project into your build:  
*Step 1.* Add the JitPack repository to your build file  
Add it in your root `settings.gradle.kts` at the end of repositories:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
            val jitpackToken = System.getProperties().getProperty("jitpackToken")
            if (jitpackToken.isNullOrEmpty().not()) {
                credentials { username = jitpackToken }
            }
        }
    }
}
```

*Step 2.* Add the dependency

```kotlin
dependencies {
    // implementation("com.github.eitanliu.databinding_starter:utils:1.0.0")
    // implementation("com.github.eitanliu.databinding_starter:binding:1.0.0")
    implementation("com.github.eitanliu.databinding_starter:starter:1.0.0")
}
```
