plugins {
    kotlin("jvm") version "2.2.20"
    id("com.gradleup.shadow") version "9.2.2"
    application
}

group = "org.exxuslee.wifiwatcher"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.telegram:telegrambots-client:9.2.0")
    implementation("org.mapdb:mapdb:3.0.9")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("org.exxuslee.wifiwatcher.MainKt")
}