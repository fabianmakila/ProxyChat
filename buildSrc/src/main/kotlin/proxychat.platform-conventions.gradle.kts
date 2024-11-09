plugins {
	id("proxychat.java-conventions")
	id("com.gradleup.shadow")
}

dependencies {
	implementation(project(":common"))
}

tasks {
	build {
		dependsOn(shadowJar)
	}
	shadowJar {
		minimize()

		destinationDirectory.set(file("${rootProject.rootDir}/dist"))
		archiveClassifier.set("")
		archiveBaseName.set("${rootProject.name}-${project.name.replaceFirstChar(Char::titlecase)}")

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
}