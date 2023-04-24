plugins {
    id("proxychat.java-conventions")
}

dependencies {
    // Libraries
    compileOnly(libs.cloud.core)
    compileOnly(libs.slf4j)
    implementation(libs.dazzleconf)
    compileOnly(libs.snakeyaml)
    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.minimessage)
}