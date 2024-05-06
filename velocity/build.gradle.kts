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
	implementation(libs.cloud.minecraftExtras)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.velocity)
}

tasks {
	shadowJar {
		sequenceOf(
			"org.incendo.cloud",
			"io.leangen",
			"space.arim",
			"org.bstats",
			"com.google.gson",
			"de.myzelyam"
		).forEach {
			relocate(it, "${project.group}.${rootProject.name.lowercase()}.dependency.$it")
		}
	}
}