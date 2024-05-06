plugins {
	id("proxychat.platform-conventions")
	alias(libs.plugins.pluginYml.bungee)
}

dependencies {
	compileOnly(libs.waterfall.api)

	implementation(libs.bstats.bungeecord)
	implementation(libs.cloud.bungeecord)
	implementation(libs.adventure.platform.bungeecord)
	implementation(libs.gson)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.bungeecord)
}

tasks {
	shadowJar {
		sequenceOf(
			"org.incendo.cloud",
			"io.leangen",
			"net.kyori",
			"space.arim",
			"org.bstats",
			"com.google.gson",
			"de.myzelyam"
		).forEach {
			relocate(it, "${project.group}.${rootProject.name.lowercase()}.dependency.$it")
		}
	}
}

bungee {
	main = "fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord"
	name = rootProject.name
	author = "FabianAdrian"
	softDepends = setOf("PartyAndFriends", "PremiumVanish")
}