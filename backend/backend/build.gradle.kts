plugins {
    id("conversation.java-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":common"))
}