plugins {
	id("proxychat.platform-conventions")
	alias(libs.plugins.pluginYml.bungee)
}

dependencies {
	compileOnly(libs.waterfall.api)

	implementation(libs.bstats.bungeecord)

	// Cloud
	implementation(libs.cloud.bungeecord)

	implementation(libs.adventure.platform.bungeecord)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.bungeecord)
}

tasks {
	shadowJar {
		sequenceOf(
			"cloud.commandframework",
			"io.leangen",
			"net.kyori",
			"space.arim",
			"org.bstats",
			"com.google.gson",
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