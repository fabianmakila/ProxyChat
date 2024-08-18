plugins {
	`kotlin-dsl`
}

dependencies() {
	implementation("com.diffplug.spotless:spotless-plugin-gradle:${libs.plugins.spotless.get().version}")
	implementation("com.gradleup.shadow:shadow-gradle-plugin:${libs.plugins.shadow.get().version}")
}