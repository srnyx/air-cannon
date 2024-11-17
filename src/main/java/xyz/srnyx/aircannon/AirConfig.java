package xyz.srnyx.aircannon;

import org.bukkit.Material;
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
import java.util.Set;
import java.util.logging.Level;


public class AirConfig {
    @NotNull private final AnnoyingResource config;

    public final long cooldown;
    public final int uses;
    public final double power;
    public final double particlePower;
    @NotNull public final EntitiesBlacklist entitiesBlacklist;
    @Nullable public final PlayableSound sound;
    @Nullable public final Object particle;
    @NotNull public final ItemStack item;
    @Nullable public final Recipe recipe;

    public AirConfig(@NotNull AirCannon plugin) {
        config = new AnnoyingResource(plugin, "config.yml");

        cooldown = config.getLong("cooldown");
        uses = config.getInt("uses", 1);
        power = config.getDouble("power", 1.5);
        particlePower = power * 5;
        entitiesBlacklist = new EntitiesBlacklist();
        sound = config.getBoolean("sound.enabled") ? config.getPlayableSound("sound").orElse(null) : null;
        item = new ItemData(plugin, config.getItemStackOptional("item").orElse(new ItemStack(Material.IRON_HOE)))
                .setChain(AirCannon.ITEM_KEY, true)
                .target;
        particle = config.getBoolean("particle.enabled") ? ReflectionUtility.getEnumValue(1, 9, 0, RefParticle.PARTICLE_ENUM, config.getString("particle.particle", "CLOUD")) : null;
        recipe = config.getBoolean("recipe.enabled") ? config.getRecipe("recipe", result -> item).orElse(null) : null;
    }

    public class EntitiesBlacklist {
        @NotNull public Set<EntityType> list = new HashSet<>();
        public boolean treatAsWhitelist = false;

        public EntitiesBlacklist() {
            final ConfigurationSection entities = config.getConfigurationSection("entities-blacklist");
            if (entities == null) return;
            // list
            for (final String entity : entities.getStringList("list")) try {
                list.add(EntityType.valueOf(entity.toUpperCase()));
            } catch (final IllegalArgumentException e) {
                AnnoyingPlugin.log(Level.WARNING, "Invalid entity type in config.yml: " + entity);
            }
            // treatAsWhitelist
            treatAsWhitelist = entities.getBoolean("treat-as-whitelist");
        }
    }
}
