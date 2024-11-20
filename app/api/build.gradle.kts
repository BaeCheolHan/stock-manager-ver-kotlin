dependencyManagement {
    imports {
        mavenBom(Libraries.Spring.cloud)
    }
}
dependencies {
    implementation(Libraries.Spring.bootStarter)
    implementation(Libraries.Spring.bootStarterWeb)
    implementation(Libraries.Spring.openfeign)
    implementation(Libraries.Kotlin.reflect)
    implementation(project(Modules.domain))
    api(project(Modules.Infra.rdb))
    api(project(Modules.Infra.redis))
    api(project(Modules.Infra.mongodb))
    api(project(Modules.Infra.webAdapter))
    implementation(Libraries.Kotlin.jackson)
}
