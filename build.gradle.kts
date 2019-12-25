import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven("http://maven.aliyun.com/nexus/content/groups/public/")
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.3.50")
    }
}

plugins {
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

group = "vip.mystery0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("http://maven.aliyun.com/nexus/content/groups/public/")
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    compileOnly("vip.mystery0.tools:java.tools:1.2.3")
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.data:spring-data-redis")
    compileOnly("org.springframework:spring-aspects")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

apply(from = "push.gradle.kts")