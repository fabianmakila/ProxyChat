plugins {
    java

    id("com.diffplug.spotless")
    id("net.kyori.indra")
}

group = "fi.fabianadrian"
version = "1.0.0-SNAPSHOT"
description = "A simple chat plugin for Minecraft proxies."

indra {
    javaVersions {
        target(11)
    }
}

spotless {
    java {
        indentWithSpaces(4)
        trimTrailingWhitespace()
        removeUnusedImports()
    }
}