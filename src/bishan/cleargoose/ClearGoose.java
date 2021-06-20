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
