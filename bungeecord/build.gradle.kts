plugins {
    id("proxychat.java-conventions")
    alias(libs.plugins.shadow)

    id("net.minecrell.plugin-yml.bungee") version "0.5.1"
}

dependencies {
    implementation(projects.common)

    implementation(libs.waterfall.api)

    implementation(libs.cloud.bungeecord)
    implementation(libs.adventure.platform.bungeecord)
}

bungee {
    main = "fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord"
    name = rootProject.name
    author = "FabianAdrian"
}