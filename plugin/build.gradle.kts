plugins {
    id("proxychat.java-conventions")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    api(projects.api)

    //Velocity
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    //Libraries
    implementation("cloud.commandframework:cloud-velocity:1.7.0")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("org.spongepowered:configurate-gson:4.1.2")
    implementation("org.bstats:bstats-velocity:3.0.0")
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "cloud.commandframework",
            "net.kyori.adventure.text.minimessage",
            "org.spongepowered.configurate",
            "org.bstats"
        ).forEach { pkg ->
            relocate(pkg, "${rootProject.group}.${rootProject.name.toLowerCase()}.lib.$pkg")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}