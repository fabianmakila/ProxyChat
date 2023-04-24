plugins {
    id("proxychat.java-conventions")
}

dependencies {
    // Libraries
    api(libs.cloud.core)
    api(libs.slf4j)
    implementation(libs.dazzleconf)
    compileOnly(libs.snakeyaml)
    api(libs.gson)
    api(libs.guava)
    api(libs.minimessage)
}