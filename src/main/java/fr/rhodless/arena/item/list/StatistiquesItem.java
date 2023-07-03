package fr.rhodless.arena.item.list;

import fr.rhodless.arena.item.Item;
import fr.rhodless.arena.menu.StatistiquesMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatistiquesItem extends Item {
    @Override
    public ItemStack getItemStack() {
        return format(new ItemStack(Material.ENCHANTED_BOOK), "Statistiques").toItemStack();
    }

    @Override
    public int getSlot() {
        return 7;
    }

    @Override
    public void onClick(Player player) {
        new StatistiquesMenu().openMenu(player);
    }
}
