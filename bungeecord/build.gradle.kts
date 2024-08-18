plugins {
	id("proxychat.platform-conventions")
	alias(libs.plugins.pluginYml.bungee)
	alias(libs.plugins.run.waterfall)
}

dependencies {
	compileOnly(libs.bungeecord.api)

	implementation(libs.adventure.minimessage)
	implementation(libs.adventure.platform.bungeecord)
	implementation(libs.bstats.bungeecord)
	implementation(libs.cloud.bungeecord)
	implementation(libs.slf4j)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.bungeecord)
}

tasks {
	runWaterfall {
		waterfallVersion("1.21")
	}
	shadowJar {
		sequenceOf(
			"io.leangen",
			"net.kyori",
			"org.bstats",
			"org.incendo.cloud",
			"org.slf4j",
			"space.arim"
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