plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "597dae2"
    id("com.gradleup.shadow") version "9.6.1"
    id("me.modmuss50.mod-publish-plugin") version "675051c"
    id("io.papermc.hangar-publish-plugin") version "0.1.4"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "xyz.srnyx"
description = "Fundy's Air Cannon item but as a plugin for your server!"

galaxy {
    minecraft {
        spigotAPI("1.8.8")
        annoyingAPI("c5c9987")

        pluginYml {
            developerData(SRNYX)

            command("aircannon") {
                description = "Give/reload the Air Cannon plugin"

                permission("command")
            }
        }

        platformPublishing {
            github("srnyx/air-cannon")
            modrinth("CF0dn4pJ")
            hangar("AirCannon")
            spigot("112698")
            curseforge("911695")

            projectData("air-cannon")
        }
    }
}
