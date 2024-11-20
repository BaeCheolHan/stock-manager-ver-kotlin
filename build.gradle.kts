import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version Versions.Spring.boot
    id("io.spring.dependency-management") version Versions.Spring.dependencyManagementPlugin
//    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlint
    kotlin("jvm") version Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
    kotlin("kapt") version Versions.kotlin
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
//    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    group = "kr.pe.hws.stockmanager"

    configurations {
        java.sourceCompatibility = JavaVersion.VERSION_17
        java.targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(project(":app:api"))
        testImplementation("org.jetbrains.kotlin:kotlin-test")
        api(Libraries.Spring.bootStarterTest)
        api(Libraries.Kotlin.jackson)
        api("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2") // Kotlin 지원
        api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2") // Java Time 지원
    }

    val bootJar = tasks.bootJar.get()
    val jar = tasks.jar.get()

    if (project.path.startsWith(":app:")) {
        bootJar.enabled = true
        jar.enabled = true
    } else {
        bootJar.enabled = false
        jar.enabled = true
    }
    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "17"
            }
            dependsOn(processResources) // kotlin 에서 ConfigurationProperties
        }

        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "17"
            }
            dependsOn(processResources) // kotlin 에서 ConfigurationProperties
        }
        test {
            useJUnitPlatform()
        }
    }
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}
