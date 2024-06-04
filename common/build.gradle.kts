plugins {
    id("conversation.java-conventions")
}

dependencies {
    compileOnlyApi(libs.slf4j)
    compileOnly(libs.adventure.api)
    compileOnlyApi(libs.dazzleconf)
    compileOnlyApi(libs.miniplaceholders)
}