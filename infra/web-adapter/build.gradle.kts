dependencyManagement {
    imports {
        mavenBom(Libraries.Spring.cloud)
    }
}
dependencies {
    api(Libraries.Spring.openfeign)
    implementation(Libraries.Spring.bootStarterWeb)
    implementation(project(Modules.domain))
    implementation(project(Modules.Infra.common))
    implementation(project(Modules.Infra.redis))
    testImplementation(Libraries.Spring.bootStarterTest)
    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation(project(Modules.api))
}

tasks.register<Copy>("copy-dev") {
    from(file("$projectDir/../../StockManager-private/resources/dev/application-api.yml"))
    into("$projectDir/src/main/resources")
}

tasks.register<Copy>("copy-prod") {
    from(file("$projectDir/../../StockManager-private/resources/prod/application-api.yml"))
    into("$projectDir/src/main/resources")
}
