plugins {
    id("conversation.java-conventions")
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginYml.paper)
}

dependencies {
    compileOnly(libs.backend.paper)

    implementation(project(":backend:backend"))
    implementation(project(":common")) // TODO temporal fix, remove when possible
}