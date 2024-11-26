plugins {
    kotlin("jvm")
}

group = "kr.pe.hws.stockmanager"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot-starter-logging") // 기본 로깅
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}