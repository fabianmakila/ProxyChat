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