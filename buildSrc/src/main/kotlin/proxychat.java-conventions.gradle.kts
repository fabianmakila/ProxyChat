plugins {
    java

    id("com.diffplug.spotless")
    id("net.kyori.indra")
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

indra {
    javaVersions {
        target(11)
    }
}

spotless {
    java {
        //importOrder() - Broken currently
        indentWithSpaces(4)
        trimTrailingWhitespace()
        removeUnusedImports()
    }
}