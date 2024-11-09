plugins {
	id("proxychat.platform-conventions")
	alias(libs.plugins.resourceFactory.velocity)
	alias(libs.plugins.run.velocity)
}

dependencies {
	compileOnly(libs.velocity.api)

	implementation(libs.bstats.velocity)
	implementation(libs.cloud.velocity)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.velocity)
}

tasks {
	runVelocity {
		velocityVersion("3.3.0-SNAPSHOT")
	}
	shadowJar {
		sequenceOf(
			"org.incendo.cloud",
			"io.leangen",
			"space.arim",
			"org.bstats"
		).forEach {
			relocate(it, "${project.group}.${rootProject.name.lowercase()}.dependency.$it")
		}
	}
}

velocityPluginJson {
	main = "fi.fabianadrian.proxychat.velocity.ProxyChatVelocity"
	id = "proxychat"
	name = rootProject.name
	url = "https://github.com/fabianmakila/ProxyChat"
	authors = listOf("FabianAdrian")
	dependencies {
		dependency("partyandfriends", optional = true)
		dependency("premiumvanish", optional = true)
	}
}