plugins {
	id("conversation.java-conventions")
	alias(libs.plugins.shadow)
}

dependencies {
	// Velocity
	compileOnly(libs.proxy.velocity)
	annotationProcessor(libs.proxy.velocity)

	implementation(libs.bstats.velocity)
	implementation(libs.cloud.velocity)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.velocity)
}

tasks {
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