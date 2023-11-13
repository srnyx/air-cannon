package xyz.srnyx.aircannon;

import org.bukkit.Bukkit;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.aircannon.commands.AirCannonCmd;
import xyz.srnyx.aircannon.listeners.PlayerListener;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;


public class AirCannon extends AnnoyingPlugin {
    @NotNull public AirConfig config = new AirConfig(this);

    public AirCannon() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("air-cannon"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("112698")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(19840))
                .registrationOptions.toRegister(this, AirCannonCmd.class, PlayerListener.class);
    }

    @Override
    public void enable() {
        if (config.recipe != null) Bukkit.addRecipe(config.recipe);
    }

    @Override
    public void reload() {
        config = new AirConfig(this);
    }
}
