plugins {
	id("proxychat.java-conventions")
	id("com.github.johnrengelman.shadow")
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
	}
}