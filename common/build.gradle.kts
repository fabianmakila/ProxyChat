plugins {
    id("proxychat.java-conventions")
}

dependencies {
    // Libraries
    compileOnly(libs.cloud.core)
    compileOnly(libs.slf4j)

    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.minimessage)

    // Config
    implementation(libs.dazzleconf)
    compileOnly(libs.snakeyaml)
}