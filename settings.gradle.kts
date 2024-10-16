include(":app")
include(":utils")
include(":binding")
include(":starter")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            setUrl("https://jitpack.io")
            val jitpackToken = System.getProperties().getProperty("jitpackToken")
            if (jitpackToken.isNullOrEmpty().not()) {
                credentials { username = jitpackToken }
            }
        }
    }
}

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

