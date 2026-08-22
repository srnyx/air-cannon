package xyz.srnyx.aircannon.stats;

import dev.faststats.Metrics;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.aircannon.AirCannon;
import xyz.srnyx.annoyingapi.stats.loader.FastStatsLoader;


public class FastStats extends FastStatsLoader {
    @NotNull private final AirCannon plugin;

    public FastStats(@NotNull AirCannon plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AirCannon getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getId() {
        return "c81dac7c92eb8e6e3009273d6e0e404b";
    }

    @Override
    public void mutateMetricsFactory(@NotNull Metrics.Factory factory) {
        factory.addMetric(config("config", () -> plugin.config));
    }
}
