package bishan.cleargoose;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerDeathInfo {
    final Location deathLocation;
    final String name;
    private ArrayList<ItemStack> items;

    public PlayerDeathInfo(String name, Location deathLocation, List<ItemStack> items) {
        this.deathLocation = deathLocation;
        this.items = new ArrayList<>(items);
        this.name = name;
    }

    public boolean contains(ItemStack stack) {
        return items.contains(stack);
    }

    /**
     * @param stack Item to remove cross off list
     */
    public void popItem(ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSimilar(stack)) {
                items.remove(i);
                return;
            }
        }
    }

    public boolean shouldDelete() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("PlayerDeathInfo: [");
        items.forEach(item -> builder
                .append(item.toString())
                .append(", "));
        builder.append("]");
        return builder.toString();
    }
}
