plugins {
	id("proxychat.java-conventions")
}

dependencies {
	// Libraries
	compileOnly(libs.cloud.core)
	compileOnly(libs.adventure.api)
	compileOnly(libs.slf4j)
	compileOnly(libs.guava)
	compileOnly(libs.snakeyaml)

	implementation(libs.gson)
	implementation(libs.minimessage) {
		exclude("net.kyori")
	}

	// Config
	implementation(libs.dazzleconf) {
		exclude("org.yaml")
	}
}