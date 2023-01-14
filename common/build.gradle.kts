plugins {
    id("proxychat.java-conventions")
}

dependencies {
    // Libraries
    api(libs.slf4j)
    api(libs.configurate.hocon)
    api(libs.gson)
    api(libs.minimessage)
}