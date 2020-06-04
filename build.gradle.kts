import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        val nexusUrl = System.getenv("NEXUS_URL") ?: PublishConfig.NEXUS_URL
        maven("$nexusUrl/repository/maven-public/")
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.3.72")
    }
}

plugins {
    id("org.springframework.boot") version "2.2.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "vip.mystery0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val nexusUrl = System.getenv("NEXUS_URL") ?: PublishConfig.NEXUS_URL

repositories {
    maven("$nexusUrl/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.3.72")
    compileOnly("vip.mystery0.tools:java.tools:1.2.9")
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.springframework.data:spring-data-redis")
    compileOnly("org.springframework:spring-aspects")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")
    testImplementation("vip.mystery0.tools:java.tools:1.2.9")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("junit:junit:4.13")
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
    PublishConfig.sourceFiles = sourceSets["main"].java.srcDirs
}

apply(from = "push.gradle.kts")