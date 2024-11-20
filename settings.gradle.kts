rootProject.name = "stock-manager-ver-kotlin"
include(
    "app:api",
    "domain",
    "infra:rdb",
    "infra:redis",
    "infra:mongodb",
    "infra:web-adapter"
)
