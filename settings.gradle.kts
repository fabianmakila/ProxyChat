rootProject.name = "Conversation"

include(
	"proxy:bungeecord",
	"proxy:proxy",
	"proxy:velocity",
	"backend:backend",
	"backend:paper",
	"common"
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
