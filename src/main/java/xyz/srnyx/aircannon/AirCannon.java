package xyz.srnyx.aircannon;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.aircannon.config.AirConfig;
import xyz.srnyx.aircannon.messages.ACMessagesProvider;
import xyz.srnyx.aircannon.stats.FastStats;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.file.okaeri.migration.NestedSoundMigration;

import java.util.logging.Level;


public class AirCannon extends AnnoyingPlugin {
    @NotNull public static final String ITEM_KEY = "air_cannon";

    public AirConfig config;

    public AirCannon() {
        options.statsOptions(statsOptions -> statsOptions
                .bStats(bStatsOptions -> bStatsOptions.id(19840))
                .fastStats(fastStatsOptions -> fastStatsOptions.loader(FastStats.class)));
    }

    @Override @NotNull
    public ACMessagesProvider getMessages() {
        return (ACMessagesProvider) super.getMessages();
    }

    @Override
    public void load() {
        config = configLoader.build(builder -> builder
                .config(new AirConfig(this))
                .internalStateMigrations(new NestedSoundMigration("sound")));
    }

    @Override
    public void enable() {
        if (config.recipe.enabled) try {
            Bukkit.addRecipe(config.recipe.recipe);
        } catch (final IllegalStateException e) {
            logErrorTrack(Level.SEVERE, "Failed to add Air Cannon recipe", e);
        }
    }

    @Override
    public void reload() {
        config.reload();
    }
}
