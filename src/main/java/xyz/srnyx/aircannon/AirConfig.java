package xyz.srnyx.aircannon;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.aircannon.reflection.org.bukkit.RefParticle;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.data.ItemData;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.utility.ReflectionUtility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;


public class AirConfig {
    @NotNull private final AirCannon plugin;
    public final long cooldown;
    public final int uses;
    public final double power;
    @Nullable public final Set<EntityType> entitiesBlacklist;
    public final boolean treatBlacklistAsWhitelist;
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

        // entitiesBlacklist & treatBlacklistAsWhitelist
        final ConfigurationSection entities = config.getConfigurationSection("worlds-blacklist");
        if (entities == null) {
            treatBlacklistAsWhitelist = false;
            entitiesBlacklist = null;
        } else {
            treatBlacklistAsWhitelist = entities.getBoolean("treat-as-whitelist");
            entitiesBlacklist = getEntitiesBlacklist(entities);
        }

        particle = config.getBoolean("particle.enabled") ? ReflectionUtility.getEnumValue(1, 9, 0, RefParticle.PARTICLE_ENUM, config.getString("particle.particle", "CLOUD")) : null;
        recipe = config.getBoolean("recipe.enabled") ? config.getRecipe("recipe", this::applyData) : null;
        item = recipe != null ? recipe.getResult() : applyData(config.getItemStack("recipe.result"));
    }

    @Nullable
    private Set<EntityType> getEntitiesBlacklist(@NotNull ConfigurationSection section) {
        // Empty blacklist
        final List<String> list = section.getStringList("list");
        if (list.isEmpty() && !treatBlacklistAsWhitelist) return null;

        // Return list of entities
        final Set<EntityType> blacklist = new HashSet<>();
        for (final String entity : list) try {
            blacklist.add(EntityType.valueOf(entity.toUpperCase()));
        } catch (final IllegalArgumentException e) {
            AnnoyingPlugin.log(Level.WARNING, "Invalid entity type in config.yml: " + entity);
        }
        return blacklist;
    }

    @Nullable
    private ItemStack applyData(@Nullable ItemStack itemStack) {
        return itemStack == null ? null : new ItemData(plugin, itemStack).set("air_cannon", true).target;
    }
}
