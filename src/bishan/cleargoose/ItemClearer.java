package bishan.cleargoose;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.time.Duration;
import java.util.*;

enum ClearLagTimestamp {
    THREE_SECONDS(Duration.ofSeconds(3), "Ground items will be cleared in 3"),
    TWO_SECONDS(Duration.ofSeconds(2), "Ground items will be cleared in 2"),
    ONE_SECONDS(Duration.ofSeconds(1), "Ground items will be cleared in 1");

    private final Duration duration;
    private final String msg;

    ClearLagTimestamp(Duration duration, String msg) {
        this.duration = duration;
        this.msg = msg;
    }
}

public class ItemClearer implements CommandExecutor {
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
                .setExecutor(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        return true;
    }

    public void queueClearItem(ClearOptions options) {
        plugin
                .getServer()
                .getScheduler()
                .scheduleSyncDelayedTask(
                        plugin,
                        () -> clearItems(options),
                        options.delay.toMillis()
                );
    }

    public void clearItems(ClearOptions options) {
        World world = Bukkit.getWorld("world");
        assert world != null;

        world
                .getEntitiesByClass(Item.class)
                .stream()
                .filter(entity -> !deathBlacklist.contains(entity.getUniqueId()))
                .forEach(Entity::remove);
    }


    public static class ClearOptions {
        public Duration delay = Duration.ofSeconds(5);
        public boolean ignoreDeathItems = true;


        public ClearOptions() {
        }
    }
}
