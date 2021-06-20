package bishan.cleargoose;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class ClearGoose extends JavaPlugin implements Listener {
    public enum Commands {
        CLEAR_ITEMS("clearitems");

        final String cmd;

        Commands(String cmd) {
            this.cmd = cmd;
        }

        public void setExecutor(CommandExecutor executor, JavaPlugin plugin) {
            getCommand(plugin)
                    .setExecutor(executor);
        }

        public PluginCommand getCommand(JavaPlugin plug) {
            return Objects.requireNonNull(plug.getCommand(cmd));
        }

        public String getCommandName() {
            return this.cmd;
        }
    }

    ItemClearer itemClearer;

    @Override
    public void onEnable() {
        getLogger()
                .info("Clear Goose Enabled");


        // Setup item clearer manager
        itemClearer = new ItemClearer(this);
    }


    // Custom Config

    @Override
    public void onDisable() {
        getLogger().info("Clear goose Disabled");
    }
}
