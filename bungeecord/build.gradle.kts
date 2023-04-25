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

bungee {
    main = "fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord"
    name = rootProject.name
    author = "FabianAdrian"
}