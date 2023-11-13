package xyz.srnyx.aircannon.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.aircannon.AirCannon;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.data.ItemData;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static xyz.srnyx.aircannon.reflection.org.bukkit.RefParticle.PARTICLE_CLOUD;
import static xyz.srnyx.aircannon.reflection.org.bukkit.RefWorld.WORLD_SPAWN_PARTICLE_METHOD;


public class PlayerListener extends AnnoyingListener {
    @NotNull private final AirCannon plugin;
    @Nullable private final Map<UUID, PlayerData> data;

    public PlayerListener(@NotNull AirCannon plugin) {
        this.plugin = plugin;
        data = plugin.config.cooldown > 0 || plugin.config.uses > 0 ? new HashMap<>() : null;
    }

    @Override @NotNull
    public AirCannon getAnnoyingPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        final Action action = event.getAction();
        final ItemStack stack = event.getItem();
        if (action.equals(Action.PHYSICAL) || stack == null || !new ItemData(plugin, stack).has("air_cannon")) return;
        event.setCancelled(true);

        // Check cooldown/uses
        final Player player = event.getPlayer();
        if (data != null) {
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
        final World world = player.getWorld();
        final Location location = player.getLocation();
        final Vector direction = location.getDirection();

        // Pull/push
        double velocityMultiplier = plugin.config.power;
        final Location particleLocation = location.clone();
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            velocityMultiplier *= -1;
        } else {
            particleLocation.add(direction.clone().multiply(plugin.config.particlePower));
        }

        // Apply velocity, spawn particle, and play sound
        player.setVelocity(direction.clone().multiply(velocityMultiplier));
        if (WORLD_SPAWN_PARTICLE_METHOD != null && PARTICLE_CLOUD != null) try {
            WORLD_SPAWN_PARTICLE_METHOD.invoke(world, PARTICLE_CLOUD, particleLocation, 15, 1.5, 1.5, 1.5, 0.1);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
