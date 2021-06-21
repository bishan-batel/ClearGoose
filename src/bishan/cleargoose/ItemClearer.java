package bishan.cleargoose;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemClearer {
    ClearGoose plugin;
    DeathListener listener;
    List<UUID> deathBlacklist;

    public ItemClearer(ClearGoose plugin) {
        this.plugin = plugin;
        this.listener = new DeathListener(this);
        this.deathBlacklist = new ArrayList<>();

        // Registers listener
        plugin.getServer()
                .getPluginManager()
                .registerEvents(listener, plugin);

        // Gives class control of clear items command
        ClearGoose
                .Commands
                .CLEAR_ITEMS
                .setExecutor(this::onClearItemCommand, plugin);
    }

    public boolean onClearItemCommand(CommandSender sender, Command cmd, String name, String[] args) {
        // Command Layout: /clearitems

        if (args.length > 2) {
            sender.sendMessage("Invalid number of arguments");
            return false;
        }

        ClearOptions options = new ClearOptions();
        queueClearItems(new ClearOptions());

        return true;
    }

    public void queueClearItems(ClearOptions options) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        scheduler.scheduleSyncDelayedTask(plugin,
                () -> Bukkit.broadcastMessage("Items are being cleared in 3"),
                secondsToTicks(options.delay.getSeconds() + 1)
        );

        scheduler.scheduleSyncDelayedTask(plugin,
                () -> Bukkit.broadcastMessage("Items are being cleared in 2"),
                secondsToTicks(options.delay.getSeconds() + 2)
        );

        scheduler.scheduleSyncDelayedTask(plugin,
                () -> Bukkit.broadcastMessage("Items are being cleared in 1"),
                secondsToTicks(options.delay.getSeconds() + 3)
        );

        // Gives delayed task to scheduler
        scheduler.scheduleSyncDelayedTask(
                plugin,
                () -> clearItems(options),
                secondsToTicks(options.delay.getSeconds() + 4)
        );
    }

    public void clearItems(ClearOptions options) {
        List<World> worlds = Bukkit.getWorlds();


        var count = new AtomicInteger();

        worlds
                .forEach(world -> world

                        .getEntitiesByClass(Item.class).stream()
                        // Removes all entities who is UID is not on blacklist
                        .filter(entity -> !deathBlacklist.contains(entity.getUniqueId()))
                        .forEach(entity -> {
                            entity.remove();
                            count.incrementAndGet();
                        })
                );

        // Removes any blacklisted UID that does not lead to a still-living entity
        deathBlacklist
                // Removes blacklisted UID if
                .removeIf(blacklistedUID ->
                        worlds.stream()
                                // there is no world with a entity with a UID that matches the blacklisted item
                                .noneMatch(world -> world
                                        .getEntitiesByClass(Item.class).stream()
                                        .anyMatch(entity -> entity.getUniqueId().equals(blacklistedUID))
                                )
                );

        Bukkit.broadcastMessage("Cleared " + count + " entities");
    }

    static long secondsToTicks(long seconds) {
        return seconds * 20;
    }

    @Nonnull
    public static class ClearOptions {
        public Duration delay;

        // TODO implement IgnoreNewItems
        public boolean ignoreDeathItems, ignoreNewItems;


        public ClearOptions() {
            // Defaults
            this(
                    Duration.ofSeconds(0),
                    true,
                    true
            );
        }

        public ClearOptions(Duration delay, boolean ignoreDeath, boolean ignoreNew) {
            this.delay = delay;
            ignoreDeathItems = ignoreDeath;
            ignoreNewItems = ignoreNew;
        }
    }
}
