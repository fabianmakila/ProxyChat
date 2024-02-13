plugins {
	id("proxychat.platform-conventions")
}

dependencies {
	// Velocity
	compileOnly(libs.velocity.api)
	annotationProcessor(libs.velocity.api)

	implementation(libs.bstats.velocity)

	// Cloud
	implementation(libs.cloud.velocity)
	implementation(libs.cloud.translations.velocity)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.velocity)
}

tasks {
	shadowJar {
		sequenceOf(
			"cloud.commandframework",
			"io.leangen",
			"space.arim",
			"org.bstats",
			"com.google.gson"
		).forEach {
			relocate(it, "${project.group}.${rootProject.name.lowercase()}.dependency.$it")
		}
	}
}