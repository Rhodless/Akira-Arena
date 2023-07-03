package fr.rhodless.arena.menu;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.module.Module;
import fr.rhodless.arena.module.kit.QuickKitInfo;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.menu.api.Button;
import fr.rhodless.menu.api.pagination.PaginatedMenu;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ViewKitsMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Kits";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Module module = Arena.getInstance().getModuleHandler().getModule();
        List<QuickKitInfo> kits = new ArrayList<>(module.getKits());
        Collections.reverse(kits);
        for (QuickKitInfo kit : kits) {
            buttons.put(buttons.size(), new KitButton(kit));
        }

        return buttons;
    }

    @Override
    public int getGlassColor() {
        return 14;
    }

    @RequiredArgsConstructor
    private static class KitButton extends Button {
        private final QuickKitInfo kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
            ItemBuilder itemBuilder = new ItemBuilder(kit.getItemStack().clone()).setName("&6&l" + kit.getName() + " &f▎ &7&lCLIC").setLore();

            List<String> lore = new ArrayList<>(kit.getDescription());
            lore.add("");
            if (profile.getSelectedKit() != null && profile.getSelectedKit().getName().equals(kit.getName())) {
                itemBuilder.setAmount(1);
                lore.add("&f&l» &cVous avez déjà ce kit");
                itemBuilder.addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags();
            } else {
                itemBuilder.setAmount(0);
                lore.add("&f&l» &aCliquez-ici pour choisir ce kit");
            }

            return itemBuilder.setLore(lore).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
            profile.setSelectedKit(kit);

            new ViewKitsMenu().openMenu(player);
        }
    }
}
