package fr.rhodless.arena.module.power.defaults;

import fr.rhodless.arena.module.power.Power;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public abstract class ItemPower extends Power {
    public ItemPower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }

    public ItemStack getItemStack() {
        return format(Material.ENCHANTED_BOOK, getPowerName()).toItemStack();
    }

    public boolean isSimilar(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) return false;

        String itemName = removeClick(itemStack);
        String powerName = removeClick(getItemStack());

        return itemName.equals(powerName) && itemStack.getType().equals(getItemStack().getType());
    }

    private String removeClick(ItemStack itemStack) {
        String name = itemStack.getItemMeta().getDisplayName();
        if (name.contains("-droit")) name = name.replace("-droit", "");
        if (name.contains("-gauche")) name = name.replace("-gauche", "");
        return name;
    }

    protected ItemBuilder format(ItemStack it, String name) {
        String clic = " &7▪ &fClic";
        if (this instanceof RightClickPower || this instanceof RightClickPlayerPower) clic += "-droit";
        if (this instanceof LeftClickPower || this instanceof LeftClickPlayerPower) clic += "-gauche";

        return new ItemBuilder(it).setName("&c&l" + name + clic)
                .addEnchant(Enchantment.DURABILITY, 1).hideItemFlags();
    }

    protected ItemBuilder format(Material material, String name) {
        return format(new ItemStack(material), name);
    }

    public boolean shouldGiveAtDistribution() {
        return true;
    }
}
