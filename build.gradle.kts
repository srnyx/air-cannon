import xyz.srnyx.gradlegalaxy.utility.setupAnnoyingAPI
import xyz.srnyx.gradlegalaxy.utility.spigotAPI


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "1.1.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

setupAnnoyingAPI("4.1.1", "xyz.srnyx", "1.0.0", "Fundy's Air Cannon item but as a plugin for your server!")
spigotAPI("1.8.8")
