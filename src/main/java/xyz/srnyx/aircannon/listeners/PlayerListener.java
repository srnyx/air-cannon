package xyz.srnyx.aircannon.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.aircannon.AirCannon;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.data.ItemData;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import static xyz.srnyx.aircannon.reflection.org.bukkit.RefWorld.WORLD_SPAWN_PARTICLE_METHOD;


public class PlayerListener extends AnnoyingListener {
    @NotNull private final AirCannon plugin;
    @NotNull private final Map<UUID, PlayerData> data = new HashMap<>();

    public PlayerListener(@NotNull AirCannon plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AirCannon getAnnoyingPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        final Action action = event.getAction();
        final ItemStack stack = event.getItem();
        if (action.equals(Action.PHYSICAL) || stack == null || !new ItemData(plugin, stack).has(AirCannon.ITEM_KEY)) return;
        event.setCancelled(true);

        // Check cooldown/uses
        final Player player = event.getPlayer();
        if (!data.isEmpty()) {
            final long now = System.currentTimeMillis();
            final PlayerData playerData = data.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(now, plugin.config.uses));
            if (now >= playerData.cooldown) {
                playerData.uses = plugin.config.uses;
                playerData.cooldown = now + plugin.config.cooldown;
            }
            if (playerData.uses <= 0) return;
            playerData.uses--;
        }

        // Variables
        final Location location = player.getLocation();
        final Vector direction = location.getDirection();

        // Get velocity and particle location (push/pull)
        final double velocityMultiplier;
        final Location particleLocation;
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            // Push
            velocityMultiplier = -1;
            particleLocation = location; // Particle "behind" player
        } else {
            // Pull
            velocityMultiplier = 1;
            particleLocation = location.clone().add(direction.clone().multiply(plugin.config.particlePower)); // Particle in front of player
        }
        final Vector velocity = direction.clone().multiply(plugin.config.power * velocityMultiplier);

        // Affect nearby entities
        final Set<EntityType> blacklist = plugin.config.entitiesBlacklist.list;
        final boolean treatAsWhitelist = plugin.config.entitiesBlacklist.treatAsWhitelist;
        player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> treatAsWhitelist == blacklist.contains(entity.getType()))
                .forEach(entity -> entity.setVelocity(velocity));

        // Apply velocity to player
        player.setVelocity(velocity);

        // Spawn particle
        final World world = player.getWorld();
        if (WORLD_SPAWN_PARTICLE_METHOD != null && plugin.config.particle != null) try {
            WORLD_SPAWN_PARTICLE_METHOD.invoke(world, plugin.config.particle, particleLocation, 15, 1.5, 1.5, 1.5, 0.1);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            AnnoyingPlugin.log(Level.WARNING, "Failed to spawn particle", e);
        }

        // Play sound
        if (plugin.config.sound != null) plugin.config.sound.play(world, location);
    }

    private static class PlayerData {
        private long cooldown;
        private int uses;

        public PlayerData(long cooldown, int uses) {
            this.cooldown = cooldown;
            this.uses = uses;
        }
    }
}
