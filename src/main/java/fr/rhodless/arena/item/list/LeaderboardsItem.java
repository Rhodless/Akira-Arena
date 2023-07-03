package fr.rhodless.arena.item.list;

import fr.rhodless.arena.item.Item;
import fr.rhodless.arena.menu.LeaderboardsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LeaderboardsItem extends Item {
    @Override
    public ItemStack getItemStack() {
        return format(new ItemStack(Material.EMERALD), "Classements").toItemStack();
    }

    @Override
    public int getSlot() {
        return 8;
    }

    @Override
    public void onClick(Player player) {
        new LeaderboardsMenu().openMenu(player);
    }
}
