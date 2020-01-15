import java.io.File

/**
 * @author mystery0
 * Create at 2019/12/25
 */
object PublishConfig {
    const val POM_GROUP_ID = "vip.mystery0"
    const val POM_ARTIFACT_ID = "base-spring-boot-starter"
    const val POM_VERSION = "0.4.2"

    const val POM_REPO_NAME = "tools"
    const val POM_USER_ORG = "mystery00"
    const val POM_UPLOAD_NAME = "base-spring-boot-starter"
    const val POM_DESC = "SpringBoot basic framework"
    const val POM_WEBSITE = "https://github.com/Mystery0Tools/BaseSpringBoot"

    lateinit var sourceFiles: Set<File>
}