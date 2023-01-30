plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies() {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.14.0")
    implementation("net.kyori:indra-common:3.0.1")
}