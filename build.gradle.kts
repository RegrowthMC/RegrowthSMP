plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
    id("xyz.jpenilla.run-paper") version("2.2.4")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    group = "org.lushplugins"
    version = "1.1.26"

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
        compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        shadowJar {
            minimize()

            archiveFileName.set("${project.name}-${project.version}.jar")
        }

        processResources{
            expand(project.properties)

            inputs.property("version", rootProject.version)
            filesMatching("plugin.yml") {
                expand("version" to rootProject.version)
            }
        }
    }
}

subprojects {
    dependencies {
        compileOnly("org.lushplugins:LushLib:0.10.75")

        if (project.name != "common") {
            compileOnly(project(":common"))
        }
    }
}

dependencies {
    // Dependencies
    compileOnly("com.mysql:mysql-connector-j:8.3.0")
    compileOnly("org.xerial:sqlite-jdbc:3.46.0.0")

    // Libraries
    implementation("org.lushplugins:LushLib:0.10.75")

    // Modules
    implementation(project(":common"))
    implementation(project(":modules:abilities"))
    implementation(project(":modules:bookreader"))
    implementation(project(":modules:claimnotifs"))
    implementation(project(":modules:cosmetics"))
    implementation(project(":modules:crateanimation"))
    implementation(project(":modules:extraluckpermscontexts"))
    implementation(project(":modules:functions"))
    implementation(project(":modules:glassitemframes"))
    implementation(project(":modules:pinata"))
    implementation(project(":modules:recipes"))
    implementation(project(":modules:schedule"))
    implementation(project(":modules:unbreakableblocks"))
    implementation(project(":modules:utilities"))
    implementation(project(":modules:welcome"))
}

tasks {
    shadowJar {
        relocate("org.lushplugins.lushlib", "org.lushplugins.regrowthsmp.libraries.lushlib")
        relocate("fr.skytasul", "org.lushplugins.regrowthsmp.libraries.skytasul")

        minimize()
    }

    runServer {
        minecraftVersion("1.21.1")

        downloadPlugins {
            modrinth("luckperms", "v5.4.145-bukkit")
            hangar("Floodgate", "Floodgate")
            github("nulli0n", "nightcore-spigot", "v2.6.3-updated", "nightcore-2.6.3.jar")
            hangar("PlaceholderAPI", "2.11.6")
            modrinth("worldedit", "Bu1zaaoc")
            modrinth("worldguard", "7.0.12")
        }
    }
}