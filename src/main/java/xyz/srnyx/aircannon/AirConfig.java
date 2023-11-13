package xyz.srnyx.aircannon;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.aircannon.reflection.org.bukkit.RefParticle;

import xyz.srnyx.annoyingapi.data.ItemData;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.utility.ReflectionUtility;


public class AirConfig {
    @NotNull private final AirCannon plugin;
    public final long cooldown;
    public final int uses;
    public final double power;
    @Nullable public final PlayableSound sound;
    @Nullable public final Object particle;
    @Nullable public final Recipe recipe;
    @Nullable public final ItemStack item;

    public AirConfig(@NotNull AirCannon plugin) {
        this.plugin = plugin;
        final AnnoyingResource config = new AnnoyingResource(plugin, "config.yml");
        cooldown = config.getLong("cooldown");
        uses = config.getInt("uses", 1);
        power = config.getDouble("power", 1.5);
        sound = config.getBoolean("sound.enabled") ? config.getPlayableSound("sound") : null;
        particle = config.getBoolean("particle.enabled") ? ReflectionUtility.getEnumValue(1, 9, 0, RefParticle.PARTICLE_ENUM, config.getString("particle.particle", "CLOUD")) : null;
        recipe = config.getBoolean("recipe.enabled") ? config.getRecipe("recipe", this::applyData) : null;
        item = recipe != null ? recipe.getResult() : applyData(config.getItemStack("recipe.result"));
    }

    @Nullable
    private ItemStack applyData(@Nullable ItemStack itemStack) {
        return itemStack == null ? null : new ItemData(plugin, itemStack).set("air_cannon", true).target;
    }
}
