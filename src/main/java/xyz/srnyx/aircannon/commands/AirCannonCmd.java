package xyz.srnyx.aircannon.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.aircannon.AirCannon;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;


public class AirCannonCmd extends AnnoyingCommand {
    @NotNull private final AirCannon plugin;

    public AirCannonCmd(@NotNull AirCannon plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AnnoyingPlugin getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "aircannon.give";
    }

    @Override @NotNull
    public Predicate<String[]> getArgsPredicate() {
        return args -> args.length >= 1;
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        // reload
        if (sender.argEquals(0, "reload")) {
            plugin.reloadPlugin();
            new AnnoyingMessage(plugin, "reload").send(sender);
            return;
        }

        if (!sender.argEquals(0, "give")) {
            sender.invalidArgumentByIndex(0);
            return;
        }
        if (plugin.config.item == null) {
            new AnnoyingMessage(plugin, "no-item").send(sender);
            return;
        }

        // give
        if (sender.args.length == 1) {
            if (!sender.checkPlayer()) return;
            sender.getPlayer().getInventory().addItem(plugin.config.item);
            new AnnoyingMessage(plugin, "give.self").send(sender);
            return;
        }

        // give <player>
        final Player target = Bukkit.getPlayer(sender.args[1]);
        if (target == null) {
            sender.invalidArgumentByIndex(1);
            return;
        }
        target.getInventory().addItem(plugin.config.item);
        new AnnoyingMessage(plugin, "give.other")
                .replace("%player%", target.getName())
                .send(sender);
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        if (sender.args.length == 1) return Arrays.asList("reload", "give");
        return sender.argEquals(0, "give") ? BukkitUtility.getOnlinePlayerNames() : null;
    }
}
