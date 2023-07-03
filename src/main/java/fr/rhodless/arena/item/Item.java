package fr.rhodless.arena.item;

import fr.rhodless.arena.module.power.defaults.LeftClickPlayerPower;
import fr.rhodless.arena.module.power.defaults.LeftClickPower;
import fr.rhodless.arena.module.power.defaults.RightClickPlayerPower;
import fr.rhodless.arena.module.power.defaults.RightClickPower;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Item {
    public abstract ItemStack getItemStack();

    public abstract int getSlot();

    public abstract void onClick(Player player);

    protected ItemBuilder format(ItemStack it, String name) {
        String click = " &7▪ &fClic-droit";

        return new ItemBuilder(it).setName("&c&l" + name + click).hideItemFlags();
    }
}
