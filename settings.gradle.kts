import dev.kikugie.stonecutter.StonecutterSettings

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases/")
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.4"
}

extensions.configure<StonecutterSettings> {
    kotlinController = true
    centralScript = "build.gradle.kts"

    shared {
        versions(
            "1.16.5-forge",
            "1.16.5-fabric",
            "1.18.2-fabric",
            "1.18.2-forge",
            "1.19.2-fabric",
            "1.19.2-forge",
            "1.19.4-fabric",
            "1.19.4-forge",
            "1.20.1-forge",
            "1.20.1-fabric",
            "1.20.4-forge",
            "1.20.4-fabric",
            "1.20.4-neoforge",
            "1.20.6-fabric",
            "1.20.6-neoforge",
            "1.21-fabric",
            "1.21-neoforge")
        vcsVersion="1.21-fabric"
    }
    create(rootProject)
}

rootProject.name = "LoadMyChunks"