plugins {
	id("proxychat.platform-conventions")
	alias(libs.plugins.run.velocity)
}

dependencies {
	// Velocity
	compileOnly(libs.velocity.api)
	annotationProcessor(libs.velocity.api)

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