plugins {
	id("proxychat.platform-conventions")
	alias(libs.plugins.resourceFactory.bungeecord)
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
			"net.kyori",
			"org.slf4j",
		).forEach {
			relocate(it, "${project.group}.${rootProject.name.lowercase()}.dependency.$it")
		}
	}
}

bungeePluginYaml {
	main = "fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord"
	name = rootProject.name
	author = "FabianAdrian"
	softDepends = setOf("PartyAndFriends", "PremiumVanish")
}