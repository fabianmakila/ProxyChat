plugins {
    id("proxychat.java-conventions")
}

dependencies {
    // Libraries
    api(libs.cloud.core)
    api(libs.slf4j)
    api(libs.configurate.hocon)
    api(libs.gson)
    api(libs.guava)
    api(libs.minimessage)
}