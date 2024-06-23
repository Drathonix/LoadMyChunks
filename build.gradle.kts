plugins {
    `maven-publish`
    //id("fabric-loom")

    kotlin("jvm") version "1.9.22"
    id("dev.architectury.loom")
    //id("dev.kikugie.j52j")
    //id("me.modmuss50.mod-publish-plugin")
}

class Env() {
    val split = stonecutter.current.version.split("-")
    val isFabric = split[1].equals("fabric")
    val isForge = split[1].equals("forge")
    val isNeo = split[1].equals("neoforge")
    val mc_ver = split[0]
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
    val fabric_api_id = if(stonecutter.compare(env.mc_ver,"1.16.5") <= 0) "fabric" else "fabric-api"
    val java_ver = if(stonecutter.compare(env.mc_ver,"1.16.5") <= 0) 8 else if(stonecutter.compare(env.mc_ver,"1.20.4") <= 0) 17 else 21
}

class ModDependencies {
    operator fun get(name: String) = property("deps.$name").toString()
}

val mod = ModData()
val deps = ModDependencies()
val mcDep = property("mod.mc_max").toString()

stonecutter.const("fabric", env.isFabric)
stonecutter.const("forge", env.isForge)
stonecutter.const("neoforge", env.isNeo)

loom {
    //silentMojangMappingsLicense()
}

/*architectury{
    platformSetupLoomIde()
    if(env.isFabric){
        fabric()
    }
    if(env.isForge){
        forge()
    }
    if(env.isNeo){
        neoForge()
    }
}*/

version = "${mod.version}+${env.mc_ver}"
group = mod.group
base { archivesName.set(mod.id) }

loom {
    //splitEnvironmentSourceSets()

    /*mods {
        create("template") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }*/
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
}

dependencies {
    fun fapi(vararg modules: String) {
        modules.forEach { fabricApi.module(it, deps["fapi"]) }
    }

    minecraft("com.mojang:minecraft:${env.mc_ver}")
    mappings(loom.officialMojangMappings())
    val arch = if(stonecutter.compare(env.mc_ver, "1.16.5") > 0) "dev.architectury" else "me.shedaniel"
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
    if(stonecutter.compare(env.mc_ver,"1.16.5") <= 0)
    vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")
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
        "java_ver" to mod.java_ver
    )

    filesMatching("fabric.mod.json") { expand(map) }
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

/*
publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${mod.name} ${mod.version} for env.mc_ver"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    dryRun = providers.environmentVariable("MODRINTH_TOKEN")
        .getOrNull() == null || providers.environmentVariable("CURSEFORGE_TOKEN").getOrNull() == null

    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(env.mc_ver)
        requires {
            slug = "fabric-api"
        }
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add(env.mc_ver)
        requires {
            slug = "fabric-api"
        }
    }
}
*/
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