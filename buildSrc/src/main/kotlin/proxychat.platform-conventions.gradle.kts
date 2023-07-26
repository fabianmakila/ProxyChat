plugins {
    id("proxychat.java-conventions")
    id("com.modrinth.minotaur")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":common"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        destinationDirectory.set(file("${rootProject.rootDir}/dist"))
        archiveClassifier.set("")
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("i4z1bWh6")
    gameVersions.addAll(
        listOf(
            "1.13",
            "1.13.1",
            "1.13.2",
            "1.14",
            "1.14.1",
            "1.14.2",
            "1.14.3",
            "1.14.4",
            "1.15",
            "1.15.1",
            "1.15.2",
            "1.16",
            "1.16.1",
            "1.16.2",
            "1.16.3",
            "1.16.4",
            "1.16.5",
            "1.17",
            "1.17.1",
            "1.18",
            "1.18.1",
            "1.19",
            "1.19.1",
            "1.19.2",
            "1.19.3",
            "1.19.4",
            "1.20",
            "1.20.1"
        )
    )
}