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
    implementation(libs.gson)
    implementation(libs.minimessage)

    // Plugin hooks
    compileOnly(libs.partyAndFriends.velocity)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize{
            //exclude(dependency("org.bstats:bstats-velocity:.*"))
        }
        sequenceOf(
            "cloud.commandframework",
            "net.kyori.adventure.text.minimessage",
            "space.arim",
            "org.yaml",
            "org.bstats",
            "com.google.code.gson"
        ).forEach { pkg ->
            relocate(pkg, "${rootProject.group}.${rootProject.name.lowercase()}.dependency.$pkg")
        }
        destinationDirectory.set(file("${rootProject.rootDir}/dist"))
        archiveBaseName.set(rootProject.name + "-Velocity")
        archiveClassifier.set("")
    }
}