dependencies {
    api(Libraries.Spring.bootStarterDataMongodb)
    implementation(project(Modules.domain))
}

tasks.register<Copy>("copy-dev") {
    from(file("$projectDir/../../StockManager-private/resources/dev/application-mongodb.yml"))
    into("$projectDir/src/main/resources")
}

tasks.register<Copy>("copy-prod") {
    from(file("$projectDir/../../StockManager-private/resources/prod/application-mongodb.yml"))
    into("$projectDir/src/main/resources")
}
