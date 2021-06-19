package bishan.cleargoose;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DeathListener implements Listener {
    public final float MAX_DIST = 1f;
    final ClearGoose plugin;
    List<PlayerDeathInfo> deathStacks;
    List<UUID> deathBlacklist;

    public DeathListener(ClearGoose plugin) {
        this.plugin = plugin;
        deathStacks = new ArrayList<>();
        deathBlacklist = new ArrayList<>();
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {

        // Returns if there are no death stack checklists (DSC)
        if (deathStacks.isEmpty()) return;

        Location location = event.getLocation();
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();

        Bukkit.broadcastMessage(event.getEntity().getItemStack().toString() + " spawned");

        float maxDist2 = MAX_DIST * MAX_DIST;
        Optional<PlayerDeathInfo> infoOption = deathStacks
                .stream()

                // Filters to only death checklists that
                .filter(info ->
                        location.distanceSquared(info.deathLocation) < maxDist2 &&
                                info.contains(itemStack))
                // Gets first match
                .findFirst();


        // exits if no DSC was found;
        if (infoOption.isEmpty()) {
            Bukkit.broadcastMessage("   No checklist item found to cross");
            return;
        }

        // unwraps option
        PlayerDeathInfo info = infoOption.get();

        // adds the entity's UID to blacklist so it does not get cleared by cleargoose
        deathBlacklist.add(item.getUniqueId());

        // crosses off item on the death checklist

        info.popItem(itemStack);
        Bukkit.broadcastMessage("   Popped off DCS list");


//        if (info.shouldDelete()) {
//
//            // remove checklist if empty
//            deathStacks.remove(info);
//            Bukkit.broadcastMessage("-- Finished DCS for player " + info.name + " --");
//        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        var info = new PlayerDeathInfo(player.getName(), player.getLocation(), event.getDrops());
        Bukkit.broadcastMessage("Death checklist created for " + info.name);
        deathStacks.add(info);
    }
}
