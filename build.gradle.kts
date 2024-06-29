plugins {
    `maven-publish`
    //id("fabric-loom")

    kotlin("jvm") version "1.9.22"
    id("dev.architectury.loom")
    //id("dev.kikugie.j52j")
    id("me.modmuss50.mod-publish-plugin")
}

class Env() {
    val split = stonecutter.current.version.split("-")
    val loader = split[1]
    val isFabric = loader.equals("fabric")
    val isForge = loader.equals("forge")
    val isNeo = loader.equals("neoforge")
    val mc_ver = split[0]

    fun atLeast(version: String) = stonecutter.compare(mc_ver, version) >= 0
    fun atMost(version: String) = stonecutter.compare(mc_ver, version) <= 0
    fun isNot(version: String) = stonecutter.compare(mc_ver, version) != 0
}
val env = Env()

class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name").toString()
    val version = property("mod.version").toString()
    val group = property("maven_group").toString()
    val mc_min = property("mod.mc_min").toString()
    val mc_max = property("mod.mc_max").toString()
    val description = property("mod.description").toString()
    val authors = property("mod.authors").toString()
    val icon = property("mod.icon").toString()
    val fabric_common_entry = property("mod.fabric_common_entry").toString()
    val fabric_client_entry = property("mod.fabric_client_entry").toString()
    val main_mixin = property("mod.main_mixin").toString()
    val fabric_mixin = property("mod.fabric_mixin").toString()
    val issue_tracker = property("mod.issue_tracker").toString()
    val license = property("mod.license").toString()
    val github_url = property("mod.github_url").toString()
    val fabric_api_id = if(env.atMost("1.16.5")) "fabric" else "fabric-api"
    val java_ver = if(env.atMost("1.16.5")) 8 else if(stonecutter.compare(env.mc_ver,"1.20.4") <= 0) 17 else 21

}

class ModDependencies {
    operator fun get(name: String) = property("deps.$name").toString()

    val isCCTPresent = !get("mod.cct").equals("EXCLUDE")
    val isMekanismPresent = !get("mod.mekanism").equals("EXCLUDE")
}

val mod = ModData()
val deps = ModDependencies()
val mcDep = property("mod.mc_max").toString()

stonecutter.const("fabric", env.isFabric)
stonecutter.const("forge", env.isForge)
stonecutter.const("neoforge", env.isNeo)
stonecutter.const("cct", deps.isCCTPresent)
stonecutter.const("mekanism", deps.isMekanismPresent)
stonecutter.dependency("minecraft",env.mc_ver)

version = "${mod.version}+${env.mc_ver}+${env.loader}"
group = mod.group
base { archivesName.set(mod.id) }

loom {
    if (env.isForge) forge {
        mixinConfigs(
            "${mod.id}.mixins.json",
        )
    }
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository { maven("https://www.cursemaven.com") { name = "CurseForge" } }
        filter { includeGroup("curse.maven") }
    }
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
        filter { includeGroup("maven.modrinth") }
    }
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.architectury.dev/")
    maven("https://modmaven.dev/")
    maven ("https://squiddev.cc/maven/") {
        content {
            includeGroup("org.squiddev")
            includeGroup("cc.tweaked")
        }
    }
}

dependencies {
    fun fapi(vararg modules: String) {
        modules.forEach { fabricApi.module(it, deps["fapi"]) }
    }

    minecraft("com.mojang:minecraft:${env.mc_ver}")
    mappings(loom.officialMojangMappings())
    val arch = if(env.atLeast("1.18.0")) "dev.architectury" else "me.shedaniel"

    if(env.isFabric) {
        //mappings("net.fabricmc:yarn:${env.mc_ver}+build.${deps["yarn_build"]}:v2")
        modApi("${arch}:architectury-fabric:${deps["arch_ver"]}")
        modImplementation("net.fabricmc:fabric-loader:${deps["fabric_loader"]}")
        modApi("net.fabricmc.fabric-api:fabric-api:${deps["fabric_api"]}")
    }
    if(env.isForge){
        "forge"("net.minecraftforge:forge:${deps["forge_ver"]}")
        modApi("${arch}:architectury-forge:${deps["arch_ver"]}")
    }
    if(env.isNeo){
        "neoForge"("net.neoforged:neoforge:${deps["neoforge_ver"]}")
        modApi("${arch}:architectury-neoforge:${deps["arch_ver"]}")
    }
    vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")

    if(deps.isMekanismPresent){
        //TODO: fix forge specific StackOverflow with the mek modApi dependency
        if(env.isForge) {
            compileOnly("mekanism:Mekanism:${deps["mod.mekanism"]}")
        }
        else {
            modApi("mekanism:Mekanism:${deps["mod.mekanism"]}")
        }
    }

    if(deps.isCCTPresent) {
        if(env.atMost("1.19.2")) {
            modApi("org.squiddev:cc-tweaked-${env.mc_ver}:${deps["mod.cct"]}")
            /*if(env.isForge) {
                "forgeRuntimeLibrary"("org.squiddev:Cobalt:0.7.0")
                "forgeRuntimeLibrary"("com.jcraft:jzlib:1.1.3")
                "forgeRuntimeLibrary"("io.netty:netty-codec-http:4.1.82.Final")
                "forgeRuntimeLibrary"("io.netty:netty-codec-socks:4.1.82.Final")
                "forgeRuntimeLibrary"("io.netty:netty-handler-proxy:4.1.82.Final")
            }*/
        }
        else if(env.atLeast("1.19.4")){
            if(env.isForge || env.isNeo) {
                //compileOnly("cc.tweaked:cc-tweaked-${env.mc_ver}-core:${deps["mod.cct"]}")
                //compileOnly("cc.tweaked:cc-tweaked-${env.mc_ver}-forge-api:${deps["mod.cct"]}")
                modApi("cc.tweaked:cc-tweaked-${env.mc_ver}-forge:${deps["mod.cct"]}")
                //Fixes inability to use runClient
                if(env.atLeast("1.20")) {
                    "forgeRuntimeLibrary"("cc.tweaked:cobalt:0.9.3")
                }
                else{
                    "forgeRuntimeLibrary"("org.squiddev:Cobalt:0.7.0")
                }
                "forgeRuntimeLibrary"("com.jcraft:jzlib:1.1.3")
                "forgeRuntimeLibrary"("io.netty:netty-codec-http:4.1.82.Final")
                "forgeRuntimeLibrary"("io.netty:netty-codec-socks:4.1.82.Final")
                "forgeRuntimeLibrary"("io.netty:netty-handler-proxy:4.1.82.Final")
            }
            if(env.isFabric) {
               //compileOnly("cc.tweaked:cc-tweaked-${env.mc_ver}-fabric-api:${deps["mod.cct"]}")
                modApi("cc.tweaked:cc-tweaked-${env.mc_ver}-fabric:${deps["mod.cct"]}")
            }
        }
    }
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}

java {
    withSourcesJar()
    val java = if(mod.java_ver == 8) JavaVersion.VERSION_1_8 else if(mod.java_ver == 17) JavaVersion.VERSION_17 else JavaVersion.VERSION_21
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.processResources {
    inputs.property("id", mod.id)
    inputs.property("name", mod.name)
    inputs.property("version", mod.version)
    inputs.property("description", description)
    inputs.property("authors", mod.authors)
    inputs.property("github_url", mod.github_url)
    inputs.property("icon", mod.icon)
    inputs.property("fabric_common_entry", mod.fabric_common_entry)
    inputs.property("fabric_client_entry", mod.fabric_client_entry)
    inputs.property("main_mixin", mod.main_mixin)
    inputs.property("fabric_mixin", mod.fabric_mixin)
    inputs.property("mc_min", mod.mc_min)
    inputs.property("mc_max", mod.mc_max)
    inputs.property("fabric_api", deps["fabric_api"])
    inputs.property("issue_tracker", mod.issue_tracker)
    inputs.property("fabric_api_id",mod.fabric_api_id)
    inputs.property("java_ver",mod.java_ver)
    inputs.property("arch_ver",deps["arch_ver"])
    inputs.property("loader_ver_range",if(env.isForge) deps["forge_ver_range"] else deps["neo_ver_range"])
    inputs.property("neo_ver_range",deps["neo_ver_range"])
    inputs.property("loader_ver",env.loader)
    inputs.property("license",mod.license)
    inputs.property("mandatory_indicator", if(env.isNeo) "required" else "mandatory")
    inputs.property("neo_forge_1204_mixin_field", if(env.isNeo) "[[mixins]]\nconfig=\"${mod.id}.mixins.json\"" else "")
    inputs.property("temp", "hi")



    val map = mapOf(
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "description" to mod.description,
        "authors" to mod.authors,
        "github_url" to mod.github_url,
        "icon" to mod.icon,
        "fabric_common_entry" to mod.fabric_common_entry,
        "fabric_client_entry" to mod.fabric_client_entry,
        "main_mixin" to mod.main_mixin,
        "fabric_mixin" to mod.fabric_mixin,
        "mc_min" to mod.mc_min,
        "mc_max" to mod.mc_max,
        "fabric_api" to deps["fabric_api"],
        "issue_tracker" to mod.issue_tracker,
        "fabric_api_id" to mod.fabric_api_id,
        "java_ver" to mod.java_ver,
        "loader_ver_range" to if(env.isForge) deps["forge_ver_range"] else deps["neo_loader_ver_range"],
        "loader_ver" to if(env.isForge) deps["forge_ver_range"] else deps["neo_ver_range"],
        "loader_id" to env.loader,
        "license" to mod.license,
        "mandatory_indicator" to if(env.isNeo) "required" else "mandatory",
        "neo_forge_1204_mixin_field" to if(env.isNeo) "[[mixins]]\nconfig=\"${mod.id}.mixins.json\"" else "",
        "neo_ver_range" to deps["neo_ver_range"],
        "arch_ver" to deps["arch_ver"],
        "temp" to "hi"
    )

    if(env.isForge) {
        exclude("fabric.mod.json")
        exclude("META-INF/neoforge.mods.toml")
    }
    if(env.isFabric){
        exclude("META-INF/neoforge.mods.toml")
    }
    if(env.isNeo){
        if(env.atLeast("1.20.6")) {
            exclude("META-INF/mods.toml")
        }
        else{
            exclude("META-INF/neoforge.mods.toml")
        }
        exclude("fabric.mod.json")
    }
    filesMatching("fabric.mod.json") { expand(map) }
    filesMatching("META-INF/mods.toml") { expand(map) }
    filesMatching("META-INF/neoforge.mods.toml") { expand(map) }
    filesMatching("loadmychunks.mixins.json") { expand(map) }
    filesMatching("loadmychunks.fabric.mixins.json") { expand(map) }
    filesMatching("loadmychunks.forge.mixins.json") { expand(map) }
    filesMatching("loadmychunks.neoforge.mixins.json") { expand(map) }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
    dependsOn("build")
}


publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${mod.name} ${mod.version} for ${env.mc_ver}"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add(env.loader)

    dryRun = false

    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(env.mc_ver)
        requires {
            slug = "architectury-api"
        }
        if(env.isFabric){
            requires{
                slug="fabric-api"
            }
        }
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add(env.mc_ver)
        requires {
            slug = "architectury-api"
        }
        if(env.isFabric){
            requires{
                slug="fabric-api"
            }
        }
    }
}
/*
publishing {
    repositories {
        maven("...") {
            name = "..."
            credentials(PasswordCredentials::class.java)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "${property("mod.group")}.${mod.id}"
            artifactId = mod.version
            version = env.mc_ver

            from(components["java"])
        }
    }
}
*/