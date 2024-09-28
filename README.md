# Databinding Starter

### How to

To get a Git project into your build:  
*Step 1.* Add the JitPack repository to your build file  
Add it in your root build.gradle at the end of repositories:  

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI.create("https://jitpack.io")
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
    // implementation("com.github.eitanliu.databinding_starter:binding:main-SNAPSHOT")
    implementation("com.github.eitanliu.databinding_starter:starter:main-SNAPSHOT")
}
```
