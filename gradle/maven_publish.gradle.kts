// apply { plugin("maven-publish") }
apply(plugin = "maven-publish")

val jitpack = (systemEnv("JITPACK") ?: "false").toBoolean()
fun systemEnv(key: String): String? = System.getenv(key)
fun property(key: String, project: Project? = null): String =
    project?.findProperty(key)?.toString() ?: System.getProperty(key)

afterEvaluate {
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("product") {
                from(components["release"])
                // artifact(sourcesJar)

                if (jitpack) {
                    groupId = arrayOf(systemEnv("GROUP"), systemEnv("ARTIFACT")).joinToString(".")
                    version = systemEnv("VERSION")
                } else {
                    val artifactGroup = property("ARTIFACT_GROUP", project)
                    if (artifactGroup.isNullOrEmpty().not()) groupId = artifactGroup
                    version = property("ARTIFACT_VERSION", project) ?: "1.0.0-SNAPSHOT"
                }
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
