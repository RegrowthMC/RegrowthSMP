import java.io.BufferedReader
import java.io.InputStreamReader

plugins {
    `java-library`
    id("com.gradleup.shadow") version("9.3.1")
    id("xyz.jpenilla.run-paper") version("3.1.0")
}

group = "org.lushplugins"
version = "2.0.0-alpha.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://repo.lushplugins.org/releases/") // LushLib
    maven("https://repo.lushplugins.org/snapshots/") // LushLib
    maven("https://repo.opencollab.dev/main/") // Floodgate
    maven("https://repo.william278.net/releases") // HuskClaims
    maven("https://repo.auxilor.io/repository/maven-public/") // EcoSkills
    maven("https://repo.helpch.at/releases/") // PlaceholderAPI
    maven("https://maven.enginehub.org/repo/") // WorldGuard
    maven("https://jitpack.io/") // nightcore
}

dependencies {
    // Dependencies
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")

    // Soft Dependencies
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    compileOnly(files("/libraries/Animatronicsplugin.jar"))
    compileOnly(files("/libraries/ExcellentCrates-6.2.2.jar"))
    compileOnly("com.github.nulli0n:nightcore-spigot:v2.7.8") {
        exclude("org.spigotmc")
    }
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT")

    // Libraries
    implementation("org.lushplugins.lushlib:utils:1.0.2")
    implementation("org.lushplugins.chatcolorhandler:paper:8.1.1")
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.19")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.19")
    implementation("io.github.4drian3d:jdwebhooks:1.1.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))

    registerFeature("optional") {
        usingSourceSet(sourceSets["main"])
    }

    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    shadowJar {
        enableAutoRelocation = true
        relocationPrefix = "org.lushplugins.regrowthsmp.libraries"

        minimize()

        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        inputs.property("version", rootProject.version)
        inputs.property("commit", getCurrentCommitHash())

        filesMatching("plugin.yml") {
            expand(
                "version" to rootProject.version,
                "commit" to getCurrentCommitHash()
            )
        }
    }

    runServer {
        minecraftVersion("26.2")

        downloadPlugins {
            modrinth("viaversion", "5.12.1")
            modrinth("viabackwards", "5.12.1")

            modrinth("luckperms", "v5.4.145-bukkit")
            hangar("Floodgate", "Floodgate")
            modrinth("nightcore", "2.9.4")
            hangar("PlaceholderAPI", "2.11.6")
            modrinth("packetevents", "2.10.0")
            modrinth("worldedit", "Jk1z2u7n")
            modrinth("worldguard", "7.0.14")
        }
    }
}

fun getCurrentCommitHash(): String {
    val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD").start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val commitHash = reader.readLine()
    reader.close()
    process.waitFor()
    if (process.exitValue() == 0) {
        return commitHash ?: ""
    } else {
        throw IllegalStateException("Failed to retrieve the commit hash.")
    }
}