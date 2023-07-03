package fr.rhodless.arena.menu;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.men.UsedKitsMenu;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.menu.api.Button;
import fr.rhodless.menu.api.Menu;
import fr.rhodless.menu.api.buttons.DisplayButton;
import fr.rhodless.menu.api.pagination.GlassButton;
import fr.rhodless.menu.api.utils.Heads;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StatistiquesMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Statistiques";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new GlassButton(7));
        buttons.put(1, new StatistiquesButton());
        buttons.put(2, new UsedKitsButton());
        buttons.put(3, new DisplayButton(new ItemBuilder(Heads.SOON.toItemStack()).setName("&c&lProchainement...").toItemStack()));
        buttons.put(4, new GlassButton(7));

        return buttons;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.HOPPER;
    }

    private static class StatistiquesButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);

            float kdr = profile.getDeaths() == 0 ? 0 : (float) profile.getKills() / profile.getDeaths();
            return new ItemBuilder(Material.BOOK).setName("&6&lStatistiques &f▎ &7&lINFOS").setLore(
                    "&7Toutes vos statistiques sont",
                    "&7disponibles &aici&7.",
                    "",
                    "&8Informations:",
                    "&8■ &7Kills: &a" + profile.getKills(),
                    "&8■ &7Morts: &c" + profile.getDeaths(),
                    "&8■ &7K/D: &e" + kdr,
                    "&8■ &7Kits utilisés: &e" + profile.getKitHistory().size(),
                    "&8■ &7KillStreak: &e" + profile.getKillStreak(),
                    "&8■ &7Meilleur KillStreak: &e" + profile.getHighestKillStreak(),
                    " ",
                    "&f&l» &eContactez un admin en cas d'erreur"
            ).toItemStack();
        }
    }

    private class UsedKitsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST).setName("&6&lKits Utilisés &f▎ &7&lKITS").setLore(
                    "&7Permet de voir la liste des &akits &7que",
                    "&7vous avez &butilisés&7.",
                    "",
                    "&8Informations:",
                    "&8■ &7Kits utilisés: &e" + Arena.getInstance().getProfileHandler().getProfile(player).getKitHistory().size(),
                    " ",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new UsedKitsMenu(new StatistiquesMenu()).openMenu(player);
        }
    }
}
