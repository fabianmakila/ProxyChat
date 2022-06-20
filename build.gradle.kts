plugins {
    id("com.diffplug.spotless") version "6.7.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.kyori.indra") version "2.1.1"
    java
}

group = "fi.fabianadrian"
version = "0.1.0"
description = "A simple chat plugin for Velocity."

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    //Velocity
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    //Libraries
    implementation("cloud.commandframework:cloud-velocity:1.7.0")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("org.spongepowered:configurate-gson:4.1.2")
}

indra {
    javaVersions {
        target(11)
    }
}

spotless {
    java {
        importOrder()
        indentWithSpaces(4)
        trimTrailingWhitespace()
        removeUnusedImports()
    }
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "cloud.commandframework",
            "net.kyori.adventure.text.minimessage",
            "org.spongepowered.configurate"
        ).forEach { pkg ->
            relocate(pkg, "${rootProject.group}.${rootProject.name.toLowerCase()}.lib.$pkg")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}