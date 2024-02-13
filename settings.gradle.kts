rootProject.name = "ProxyChat"

include(
	"bungeecord",
	"common",
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