dependencyManagement {
    imports {
        mavenBom(Libraries.Spring.cloud)
    }
}

dependencies {
    implementation(Libraries.Spring.bootStarter)
    implementation(Libraries.Spring.bootStarterWeb)
    implementation(Libraries.Kotlin.reflect)
    implementation(project(Modules.domain))
    api(project(Modules.Infra.rdb))
    implementation(project(Modules.Infra.redis))
    api(project(Modules.Infra.mongodb))
    api(project(Modules.Infra.webAdapter))
    implementation(Libraries.Kotlin.jackson)

    testApi("org.jetbrains.kotlin:kotlin-test") // Kotlin Test
    testApi("org.springframework.boot:spring-boot-starter-test") // Spring Test
}

tasks.register<Copy>("copy-dev") {
    from(file("$projectDir/../../StockManager-private/resources/dev/application-sns.yml"))
    into("/src/main/resources")
}

tasks.register<Copy>("copy-prod") {
    from(file("$projectDir/../../StockManager-private/resources/prod/application-sns.yml"))
    into("$projectDir/src/main/resources")
}
