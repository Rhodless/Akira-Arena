package fr.rhodless.arena.item.list;

import fr.rhodless.arena.item.Item;
import fr.rhodless.arena.menu.ViewKitsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChangeKitItem extends Item {
    @Override
    public ItemStack getItemStack() {
        return format(new ItemStack(Material.NETHER_STAR), "Changer de kit").toItemStack();
    }

    @Override
    public int getSlot() {
        return 4;
    }

    @Override
    public void onClick(Player player) {
        new ViewKitsMenu().openMenu(player);
    }
}
