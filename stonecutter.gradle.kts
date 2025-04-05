//@Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("dev.kikugie.j52j") version "1.0" apply false // Enables asset processing by writing json5 files
    id("me.modmuss50.mod-publish-plugin") version "0.5.+" apply false // Publishes builds to hosting websites
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("net.minecraftforge.gradle") version "6.0.16" apply false
}
stonecutter active "1.16.5-forge" /* [SC] DO NOT EDIT */

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledClean", stonecutter.chiseled) {
    group = "project"
    ofTask("clean")
}

stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}

stonecutter registerChiseled tasks.register("chiseledPublishModrinth", stonecutter.chiseled) {
    group = "project"
    ofTask("publishModrinth")
}

