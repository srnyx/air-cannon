package xyz.srnyx.aircannon;

import org.bukkit.Bukkit;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;


public class AirCannon extends AnnoyingPlugin {
    @NotNull public AirConfig config = new AirConfig(this);

    public AirCannon() {
        options.registrationOptions.automaticRegistration.packages(
                "xyz.srnyx.aircannon.commands",
                "xyz.srnyx.aircannon.listeners");
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
