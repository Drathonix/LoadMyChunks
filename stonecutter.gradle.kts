//@Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
    id("dev.kikugie.j52j") version "1.0" apply false // Enables asset processing by writing json5 files
    //id("me.modmuss50.mod-publish-plugin") version "0.5.+" apply false // Publishes builds to hosting websites
}
stonecutter active "1.19.2-forge" /* [SC] DO NOT EDIT */

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledClean", stonecutter.chiseled) {
    group = "project"
    ofTask("clean")
}

/*
// Publishes every version
stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}
*/

stonecutter configureEach{
    /*
    See src/main/java/com/example/TemplateMod.java
    and https://stonecutter.kikugie.dev/
    */
    // Swaps replace the scope with a predefined value
    swap("mod_version", "\"${property("mod.version")}\";")
    // Dependencies add targets to check versions against
    // Using `project.property()` in this block gets the versioned property
//    dependency("fapi", project.property("deps.fabric_api").toString())
    /*val platform = property("loom.platform").toString()
    println(platform)
    stonecutter.const("fabric", platform == "fabric" )
    stonecutter.const("forge", platform == "forge" )
    stonecutter.const("neoforge", platform == "neoforge" )

     */
}

