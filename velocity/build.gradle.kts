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
    implementation(libs.snakeyaml)
    implementation(libs.cloud.velocity)

    // Plugin hooks
    compileOnly(libs.partyAndFriends.velocity)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize()
        sequenceOf(
            "cloud.commandframework",
            "io.leangen",
            "net.kyori.adventure.text.minimessage",
            "space.arim",
            "org.yaml",
            "org.bstats",
            "com.google.gson"
        ).forEach { pkg ->
            relocate(pkg, "${project.group}.${rootProject.name.lowercase()}.dependency.$pkg")
        }
        destinationDirectory.set(file("${rootProject.rootDir}/dist"))
        archiveBaseName.set("${rootProject.name}-Velocity")
        archiveClassifier.set("")
    }
}