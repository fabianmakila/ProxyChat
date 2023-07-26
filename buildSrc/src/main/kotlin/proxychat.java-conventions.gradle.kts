plugins {
    `java-library`
    id("com.diffplug.spotless")
}

group = "fi.fabianadrian"
version = "1.0.0-beta.3"
description = "A simple chat plugin for Minecraft proxies."

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

spotless {
    java {
        indentWithSpaces(4)
        trimTrailingWhitespace()
        removeUnusedImports()
    }
}