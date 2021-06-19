package bishan.cleargoose;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClearGoose extends JavaPlugin implements Listener {
    public void clearItems() {
        World world = Bukkit.getWorld("world");
        if (world == null) return;
    }

    @Override
    public void onEnable() {
        getLogger()
                .info("Clear Goose Enabled");

        // Add death listener
        getServer()
                .getPluginManager()
                .registerEvents(new DeathListener(this), this);
    }


    // Custom Config

    @Override
    public void onDisable() {
        getLogger().info("Clear goose Disabled");
    }
}
