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
apply<MavenPlugin>()

task("sourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(PublishConfig.sourceFiles)
}

val properties = Properties()
val propertiesFile = File("local.properties")
val hasFile = if (propertiesFile.exists()) {
    properties.load(FileInputStream(propertiesFile))
    true
} else {
    false
}

val NEXUS_USER = if (hasFile) properties.getProperty("NEXUS_USER") else System.getenv("NEXUS_USER")
val NEXUS_KEY = if (hasFile) properties.getProperty("NEXUS_KEY") else System.getenv("NEXUS_KEY")
val BINTRAY_USER = if (hasFile) properties.getProperty("BINTRAY_USER") else System.getenv("BINTRAY_USER")
val BINTRAY_KEY = if (hasFile) properties.getProperty("BINTRAY_KEY") else System.getenv("BINTRAY_KEY")

val url = System.getenv("NEXUS_URL") ?: PublishConfig.NEXUS_URL

configure<PublishExtension> {
    repoName = PublishConfig.POM_REPO_NAME                // 代码仓库名称。 默认设置为“maven”
    userOrg = PublishConfig.POM_USER_ORG                  // 上传的组织名称
    groupId = PublishConfig.POM_GROUP_ID
    artifactId = PublishConfig.POM_ARTIFACT_ID            // 与groupId组合
    publishVersion = PublishConfig.POM_VERSION            // 仓库版本号
    uploadName = PublishConfig.POM_UPLOAD_NAME            // bintray中包的显示名称
    desc = PublishConfig.POM_DESC                         // 仓库说明
    website = PublishConfig.POM_WEBSITE                   // 仓库地址

    bintrayUser = BINTRAY_USER                                    // bintray账号名
    bintrayKey = BINTRAY_KEY                                      // bintray账号api key

    dryRun = false
}

//Upload Task用于发布上传
tasks {
    getByName<Upload>("uploadArchives") {
        repositories {
            withConvention(MavenRepositoryHandlerConvention::class) {
                mavenDeployer {
                    var compileVersion = "unspecific"
                    withGroovyBuilder {
                        compileVersion = if (PublishConfig.POM_SNAPSHOT) {
                            //快照版本的仓库
                            "snapshotRepository"("url" to "$url/repository/maven-snapshots/") {
                                "authentication"("userName" to NEXUS_USER, "password" to NEXUS_KEY)
                            }
                            "${PublishConfig.POM_VERSION}-SNAPSHOT"
                        } else {
                            //正式发布仓库
                            "repository"("url" to "$url/repository/maven-releases/") {
                                "authentication"("userName" to NEXUS_USER, "password" to NEXUS_KEY)
                            }
                            PublishConfig.POM_VERSION
                        }
                    }
                    println("compile version: $compileVersion")
                    pom.project {
                        withGroovyBuilder {
                            "groupId"(PublishConfig.POM_GROUP_ID)
                            "artifactId"(PublishConfig.POM_ARTIFACT_ID)
                            //打包格式
                            "packaging"("jar")
                            //版本号，如果是快照版本，其版本号后面应该添加-SNAPSHOT，否则不能正常识别上传
                            "version"(compileVersion)
                            //描述
                            "description"(PublishConfig.POM_DESC)
                        }
                    }
                }
            }
        }
    }
}

task("moveJar", Copy::class) {
    from(file("$buildDir/libs/springboot-all.jar"))
    into(file("$buildDir/libs"))
    rename("(.+)-all(.+)", "$1$2")
}

task("fuck") {
    doFirst {
        if (NEXUS_USER.isNullOrBlank() || NEXUS_KEY.isNullOrBlank()) throw kotlin.Exception("missing NEXUS_USER or NEXUS_KEY")
        if (!PublishConfig.POM_SNAPSHOT)
            if (BINTRAY_USER.isNullOrBlank() || BINTRAY_KEY.isNullOrBlank()) throw kotlin.Exception("missing BINTRAY_USER or BINTRAY_KEY")
    }
    doLast {
        exec {
            if (PublishConfig.POM_SNAPSHOT)
                commandLine("./gradlew", "clean", "sourcesJar", "shadowJar", "moveJar", "uploadArchives")
            else
                commandLine(
                    "./gradlew",
                    "clean",
                    "sourcesJar",
                    "shadowJar",
                    "moveJar",
                    "uploadArchives",
                    "bintrayUpload"
                )
        }
    }
}