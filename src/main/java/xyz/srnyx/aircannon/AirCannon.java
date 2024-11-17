package xyz.srnyx.aircannon;

import org.bukkit.Bukkit;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;

import java.util.logging.Level;


public class AirCannon extends AnnoyingPlugin {
    @NotNull public static final String ITEM_KEY = "air_cannon";

    @NotNull public AirConfig config = new AirConfig(this);

    public AirCannon() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("CF0dn4pJ"),
                        PluginPlatform.hangar(this),
                        PluginPlatform.spigot("112698")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(19840))
                .registrationOptions.automaticRegistration.packages(
                        "xyz.srnyx.aircannon.commands",
                        "xyz.srnyx.aircannon.listeners");
    }

    @Override
    public void enable() {
        if (config.recipe != null) try {
            Bukkit.addRecipe(config.recipe);
        } catch (final IllegalStateException e) {
            AnnoyingPlugin.log(Level.SEVERE, "Failed to add Air Cannon recipe", e);
        }
    }

    @Override
    public void reload() {
        config = new AirConfig(this);
    }
}
