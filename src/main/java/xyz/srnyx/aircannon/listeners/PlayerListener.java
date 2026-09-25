package xyz.srnyx.aircannon.listeners;

import com.cryptomorin.xseries.XEntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.aircannon.AirCannon;
import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.data.ItemData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


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
                playerData.cooldown = now + plugin.config.cooldown.toMillis();
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
            particleLocation = location.clone().add(direction.clone().multiply(plugin.config.power * 5)); // Particle in front of player
        }
        final Vector velocity = direction.clone().multiply(plugin.config.power * velocityMultiplier);

        // Affect nearby entities
        final Set<XEntityType> blacklist = plugin.config.entities_blacklist.list;
        final boolean treatAsWhitelist = plugin.config.entities_blacklist.treat_as_whitelist;
        player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> treatAsWhitelist == blacklist.contains(XEntityType.of(entity.getType())))
                .forEach(entity -> entity.setVelocity(velocity));

        // Apply velocity to player
        // Only if crouch_grounding false or player not crouching
        if (!plugin.config.crouch_grounding || !player.isSneaking()) {
            player.setVelocity(velocity);
        }

        // Spawn particle
        if (plugin.config.particle.enabled) plugin.config.particle.particle.spawn(particleLocation, 15, 1.5, 1.5, 1.5, 0.1);

        // Play sound
        if (plugin.config.sound.enabled) plugin.config.sound.sound.play(location);
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
