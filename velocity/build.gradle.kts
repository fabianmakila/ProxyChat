plugins {
    id("proxychat.java-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.common)

    // Velocity
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)

    // Libraries
    implementation(libs.bstats.velocity)
    implementation(libs.cloud.velocity)
    implementation(libs.gson)
    implementation(libs.minimessage)
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "cloud.commandframework",
            "net.kyori.adventure.text.minimessage",
            "space.arim",
            "org.bstats",
            "com.google.code.gson"
        ).forEach { pkg ->
            relocate(pkg, "${rootProject.group}.${rootProject.name.lowercase()}.lib.$pkg")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}