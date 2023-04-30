plugins {
    id("proxychat.java-conventions")
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginYml.bungee)
}

dependencies {
    implementation(projects.common)

    compileOnly(libs.waterfall.api)

    implementation(libs.cloud.bungeecord)
    implementation(libs.adventure.platform.bungeecord)

    // Plugin hooks
    compileOnly(libs.partyAndFriends.bungeecord)
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
            "net.kyori",
            "space.arim",
            "org.bstats",
            "com.google.gson",
        ).forEach { pkg ->
            relocate(pkg, "${rootProject.group}.${rootProject.name.lowercase()}.dependency.$pkg")
        }
        destinationDirectory.set(file("${rootProject.rootDir}/dist"))
        archiveBaseName.set(rootProject.name + "-Bungeecord")
        archiveClassifier.set("")
    }
}

bungee {
    main = "fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord"
    name = rootProject.name
    author = "FabianAdrian"
    softDepends = setOf("PartyAndFriends")
}