import com.novoda.gradle.release.PublishExtension
import com.novoda.gradle.release.ReleasePlugin
import java.io.FileInputStream
import java.util.*

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.novoda:bintray-release:0.9.2")
    }
}

apply<ReleasePlugin>()

task("sourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(PublishConfig.sourceFiles)
}

val USER = "BINTRAY_USER"
val KEY = "BINTRAY_KEY"

val properties = Properties()
val propertiesFile = File("local.properties")
val hasFile = if (propertiesFile.exists()) {
    properties.load(FileInputStream(propertiesFile))
    true
} else {
    false
}

val user = if (hasFile) properties.getProperty(USER) else System.getenv(USER)
val key = if (hasFile) properties.getProperty(KEY) else System.getenv(KEY)

configure<PublishExtension> {
    repoName = PublishConfig.POM_REPO_NAME                // 代码仓库名称。 默认设置为“maven”
    userOrg = PublishConfig.POM_USER_ORG                  // 上传的组织名称
    groupId = PublishConfig.POM_GROUP_ID
    artifactId = PublishConfig.POM_ARTIFACT_ID            // 与groupId组合
    publishVersion = PublishConfig.POM_VERSION            // 仓库版本号
    uploadName = PublishConfig.POM_UPLOAD_NAME            // bintray中包的显示名称
    desc = PublishConfig.POM_DESC                         // 仓库说明
    website = PublishConfig.POM_WEBSITE                   // 仓库地址

    bintrayUser = user                                    // bintray账号名
    bintrayKey = key                                      // bintray账号api key

    dryRun = false
}

task("moveJar", Copy::class) {
    from(file("$buildDir/libs/springboot-all.jar"))
    into(file("$buildDir/libs"))
    rename("(.+)-all(.+)", "$1$2")
}

task("fuck") {
    doFirst {
        if (user.isNullOrBlank() || key.isNullOrBlank()) throw kotlin.Exception("missing bintray.users or bintray.apikey in local.properties")
    }
    doLast {
        exec {
            commandLine("./gradlew", "clean", "sourcesJar", "shadowJar", "moveJar", "bintrayUpload")
        }
    }
}