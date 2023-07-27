plugins {
	`kotlin-dsl`
}

dependencies() {
	implementation("com.diffplug.spotless:spotless-plugin-gradle:${libs.plugins.spotless.get().version}")
	implementation("com.modrinth.minotaur:Minotaur:${libs.plugins.minotaur.get().version}")
	implementation("com.github.johnrengelman:shadow:${libs.plugins.shadow.get().version}")
}