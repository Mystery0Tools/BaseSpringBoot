import PublishConfig.configPublications
import PublishConfig.configPublish
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven("http://nexus3.mystery0.vip/repository/maven-public/")
        maven("https://maven.aliyun.com/repository/public/")
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
    }
}

plugins {
    kotlin("jvm") version "1.4.30"
    `maven-publish`
}

group = "vip.mystery0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("http://nexus3.mystery0.vip/repository/maven-public/")
    maven("https://maven.aliyun.com/repository/public/")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    api("vip.mystery0.tools:java-tools:1.4.0")
    api("org.zalando:logbook-spring-boot-starter:2.5.0")
    api("com.ainemo:http-plus-spring-boot-starter:1.2.1")
    compileOnly("org.springframework.boot:spring-boot-starter-web:2.4.0")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:2.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

java {
    withSourcesJar()
}

publishing {
    configPublish(project)

    publications {
        create<MavenPublication>("maven") {
            configPublications(project, "base-spring-boot-starter")
        }
    }
}