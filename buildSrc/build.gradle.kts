plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies() {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.8.0")
    implementation("net.kyori:indra-common:2.1.1")
}