package fr.rhodless.arena.item.list;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.config.list.ConfigInventories;
import fr.rhodless.arena.item.Item;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.profile.impl.CoreProfile;
import fr.rhodless.arena.utils.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChangeInventoryItem extends Item {
    @Override
    public ItemStack getItemStack() {
        return format(new ItemStack(Material.CHEST), "Modifier l'inventaire").toItemStack();
    }

    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public void onClick(Player player) {
        CoreProfile.resetPlayer(player);

        Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
        if (profile.getSavedInventory() == null) {
            player.getInventory().setContents(ConfigInventories.DEFAULT_INVENTORY.getValue());
        } else {
            player.getInventory().setContents(profile.getSavedInventory());
        }
        profile.setEditing(true);

        player.updateInventory();
        player.sendMessage(CC.prefix("&7Vous avez reçu l'inventaire &apar défaut&7."));
        player.sendMessage(CC.prefix("&7Vous pouvez le sauvegarder en utilisant la commande &a/save&7."));
    }
}
