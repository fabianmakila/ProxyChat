plugins {
	id("conversation.java-conventions")
	alias(libs.plugins.pluginYml.bungee)
	alias(libs.plugins.shadow)
}

dependencies {
	compileOnly(libs.proxy.bungeecord)

	implementation(libs.adventure.minimessage)
	implementation(libs.adventure.platform.bungeecord)
	implementation(libs.bstats.bungeecord)
	implementation(libs.cloud.bungeecord)
	implementation(libs.slf4j)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.bungeecord)
}

tasks {
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