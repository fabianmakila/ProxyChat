plugins {
    id("proxychat.java-conventions")
}

dependencies {
    // Libraries
    api(libs.bstats.velocity)
    api(libs.cloud.velocity)
    api(libs.configurate.hocon)
    api(libs.gson)
    api(libs.minimessage)
}