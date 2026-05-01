import java.nio.file.Files
import java.nio.file.StandardCopyOption

//@Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.14.473" apply false
    id("dev.kikugie.j52j") version "1.0" apply false // Enables asset processing by writing json5 files
    id("me.modmuss50.mod-publish-plugin") version "1.0.0" apply false // Publishes builds to hosting websites
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("lol.bai.explosion") version "0.3.1" apply false

}
stonecutter active "1.21.1-neoforge" /* [SC] DO NOT EDIT */

abstract class SymlinkBuildsTask : DefaultTask() {
    @get:Input
    abstract val libs: Property<String>
    @get:Input
    abstract val vers: Property<String>
    @get:Input
    abstract val dest: Property<String>
    @get:Input
    abstract val root: Property<Project>

    @TaskAction
    fun run() {
        val destDir = File(libs.get().plus("/").plus(dest.get()))
        Files.createDirectories(destDir.toPath())
        for (project in root.get().subprojects) {
            val projLibs = File(project.layout.buildDirectory.get().asFile.absolutePath.plus("/libs"))
            if(projLibs.exists()) {
                projLibs.listFiles()?.forEach { file ->
                    if(file.name.contains(vers.get()) && !file.name.contains("sources") && file.name.endsWith(".jar")) {
                        val destFileName = file.name.replace(vers.get()+"+", "")
                        val destFile = File(destDir, destFileName)
                        Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            }
        }
    }
}

tasks.register("updateUnchangedSymlinkBuilds", SymlinkBuildsTask::class.java) {}

tasks.named<SymlinkBuildsTask>("updateUnchangedSymlinkBuilds") {
    libs.set(rootProject.layout.buildDirectory.get().asFile.absolutePath.plus("/libs"))
    vers.set(rootProject.property("version").toString())
    dest.set("symlink")
    root.set(rootProject)
}

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("build")
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

stonecutter registerChiseled tasks.register("chiseledPublishMaven", stonecutter.chiseled) {
    group = "project"
    ofTask("publish")
}
