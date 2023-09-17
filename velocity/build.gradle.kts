plugins {
	id("proxychat.platform-conventions")
}

dependencies {
	// Velocity
	compileOnly(libs.velocity.api)
	annotationProcessor(libs.velocity.api)

	// Libraries
	implementation(libs.bstats.velocity)
	implementation(libs.snakeyaml)
	implementation(libs.cloud.velocity)

	// Plugin hooks
	compileOnly(libs.partyAndFriends.velocity)
}

tasks {
	shadowJar {
		sequenceOf(
			"cloud.commandframework",
			"io.leangen",
			"net.kyori.adventure.text.minimessage",
			"space.arim",
			"org.yaml",
			"org.bstats",
			"com.google.gson"
		).forEach {
			relocate(it, "${project.group}.${rootProject.name.lowercase()}.dependency.$it")
		}
	}
}

modrinth {
	uploadFile.set(tasks.shadowJar)
	loaders.add("velocity")
}