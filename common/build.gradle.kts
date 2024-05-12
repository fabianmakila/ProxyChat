plugins {
	id("proxychat.java-conventions")
}

dependencies {
	// Libraries
	compileOnly(libs.cloud.core)
	implementation(libs.cloud.minecraftExtras)

	compileOnly(libs.adventure.api)
	compileOnly(libs.adventure.minimessage)
	compileOnly(libs.slf4j)
	compileOnly(libs.guava)
	compileOnly(libs.snakeyaml)
	compileOnly(libs.miniplaceholders)

	compileOnlyApi(libs.premiumvanish)

	compileOnly(libs.gson)

	// Config
	implementation(libs.dazzleconf) {
		exclude("org.yaml")
	}
}