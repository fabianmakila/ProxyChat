plugins {
    java

    id("com.diffplug.spotless")
    id("net.kyori.indra")
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
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