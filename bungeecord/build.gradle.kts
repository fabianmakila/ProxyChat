plugins {
    id("proxychat.platform-conventions")

    alias(libs.plugins.pluginYml.bungee)
}

dependencies {
    compileOnly(libs.waterfall.api)

    implementation(libs.bstats.bungeecord)
    implementation(libs.cloud.bungeecord)
    implementation(libs.adventure.platform.bungeecord)

    // Plugin hooks
    compileOnly(libs.partyAndFriends.bungeecord)
    compileOnly(libs.premiumvanish)
}

tasks {
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
            relocate(pkg, "${project.group}.${rootProject.name.lowercase()}.dependency.$pkg")
        }
        archiveBaseName.set(rootProject.name + "-Bungeecord")
    }
}

bungee {
    main = "fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord"
    name = rootProject.name
    author = "FabianAdrian"
    softDepends = setOf("PartyAndFriends", "PremiumVanish")
}

modrinth {
    uploadFile.set(tasks.shadowJar)
    loaders.add("bungeecord")
}