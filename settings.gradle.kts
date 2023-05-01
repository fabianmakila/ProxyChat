enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ProxyChat"

include(
    "common",
    "bungeecord",
    "velocity"
)

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://simonsator.de/repo")
        maven("https://jitpack.io")
    }
}