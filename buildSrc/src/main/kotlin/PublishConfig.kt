import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

/**
 * @author mystery0
 * Create at 2019/12/25
 */
object PublishConfig {
    private const val POM_GROUP_ID = "vip.mystery0.tools"
    private const val POM_VERSION = "1.0.1"

    private const val PROJECT_NAME = "base-spring-boot-starter"
    private const val PROJECT_DESCRIPTION = "SpringBoot basic framework"
    private const val PROJECT_URL = "https://github.com/Mystery0Tools/BaseSpringBoot"
    private const val PROJECT_DEVELOPER_ID = "mystery0"
    private const val PROJECT_DEVELOPER_NAME = "Mystery0 M"
    private const val PROJECT_DEVELOPER_EMAIL = "mystery0dyl520@gmail.com"

    private const val NEXUS_URL = "http://nexus3.mystery0.vip/repository"
    private const val NEXUS_USER = ""
    private const val NEXUS_KEY = ""

    private fun getPublishVersion(profile: String): String =
        if (isSnapshot(profile)) "$POM_VERSION-SNAPSHOT" else POM_VERSION

    private fun getPublishUri(profile: String): String {
        val snapshotsRepoUrl = "${NEXUS_URL}/maven-snapshots/"
        val releasesRepoUrl = "${NEXUS_URL}/maven-releases/"
        return if (isSnapshot(profile)) snapshotsRepoUrl else releasesRepoUrl
    }

    private fun isSnapshot(profile: String): Boolean = profile == "dev"

    private val Project.profile get() = findProperty("profile")?.toString() ?: "dev"

    fun getPublishVersion(project: Project): String =
        if (isSnapshot(project.profile)) "$POM_VERSION-SNAPSHOT" else POM_VERSION

    fun MavenPublication.configPublications(project: Project, artifact: String) {
        groupId = POM_GROUP_ID
        artifactId = artifact
        version = getPublishVersion(project.profile)
        from(project.components["java"])

        pom {
            name.set(PROJECT_NAME)
            description.set(PROJECT_DESCRIPTION)
            url.set(PROJECT_URL)
            developers {
                developer {
                    id.set(PROJECT_DEVELOPER_ID)
                    name.set(PROJECT_DEVELOPER_NAME)
                    email.set(PROJECT_DEVELOPER_EMAIL)
                }
            }
        }
    }

    fun PublishingExtension.configPublish(project: Project) {
        repositories {
            maven {
                url = project.uri(getPublishUri(project.profile))
                credentials {
                    username = System.getenv("NEXUS_USER") ?: NEXUS_USER
                    password = System.getenv("NEXUS_KEY") ?: NEXUS_KEY
                }
            }
        }
    }
}