dependencies {
    // Dependencies
    compileOnly(files("../../libraries/Animatronicsplugin.jar"))
    compileOnly(files("../../libraries/ExcellentCrates-6.2.2.jar"))
    compileOnly("com.github.nulli0n:nightcore-spigot:v2.7.8") {
        exclude("org.spigotmc")
    }
}
