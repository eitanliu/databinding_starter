// apply { plugin("maven-publish") }
apply(plugin = "maven-publish")

val jitpack = (systemEnv("JITPACK") ?: "false").toBoolean()
fun systemEnv(key: String): String? = System.getenv(key)
fun property(key: String, project: Project? = null): String? =
    project?.findProperty(key)?.toString() ?: System.getProperty(key)

afterEvaluate {
    if (jitpack) {
        group = arrayOf(systemEnv("GROUP"), systemEnv("ARTIFACT")).joinToString(".")
        version = systemEnv("VERSION")
    } else {
        val artifactGroup = property("ARTIFACT_GROUP", project)
        if (artifactGroup.isNullOrEmpty().not()) group = artifactGroup
        version = property("ARTIFACT_VERSION", project)
            ?: version.takeUnless { it == "unspecified" } ?: "1.0.0-SNAPSHOT"
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("product") {
                println("Publication $project ===> components ${components.joinToString { it.name }}")
                components.firstOrNull {
                    it.name == "release"
                }?.let {
                    println("Publication $project ===> component from ${it.name}")
                    from(it)
                }
                // artifact(sourcesJar)

                println("Publication $project ===> $jitpack $groupId:$artifactId:$version")
            }

        }

        repositories {
            maven {
                if (!jitpack) url = uri("${rootProject.layout.buildDirectory.file("repo").get()}")
            }
        }
    }
}
