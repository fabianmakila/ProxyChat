plugins {
	id("proxychat.java-conventions")
}

dependencies {
	// Libraries
	compileOnly(libs.cloud.core)
	implementation(libs.cloud.minecraftExtras)

	compileOnly(libs.adventure.api)
	compileOnly(libs.minimessage)
	compileOnly(libs.slf4j)
	compileOnly(libs.guava)
	compileOnly(libs.snakeyaml)
	compileOnly(libs.miniplaceholders)

	api(libs.premiumvanish)

	implementation(libs.gson)

	// Config
	implementation(libs.dazzleconf) {
		exclude("org.yaml")
	}
}