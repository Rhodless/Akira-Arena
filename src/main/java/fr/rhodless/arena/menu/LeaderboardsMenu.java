package fr.rhodless.arena.menu;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.leaderboard.QuickLeaderboardData;
import fr.rhodless.menu.api.Button;
import fr.rhodless.menu.api.Menu;
import fr.rhodless.menu.api.pagination.GlassButton;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Classements";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new GlassButton(7));
        buttons.put(1, new LeaderboardButton(Material.DIAMOND_SWORD, "Kills", Arena.getInstance().getLeaderboardHandler().getKillsLeaderboard()));
        buttons.put(2, new LeaderboardButton(Material.DIAMOND_HELMET, "Morts", Arena.getInstance().getLeaderboardHandler().getDeathsLeaderboard()));
        buttons.put(3, new LeaderboardButton(Material.EMERALD, "Meilleurs KillStreak", Arena.getInstance().getLeaderboardHandler().getHighestKillStreakLeaderboard()));
        buttons.put(4, new GlassButton(7));

        return buttons;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.HOPPER;
    }

    @RequiredArgsConstructor
    private static class LeaderboardButton extends Button {
        private final Material material;
        private final String field;
        private final List<QuickLeaderboardData<Integer>> values;

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder itemBuilder = new ItemBuilder(material).setName("&6&l" + field + " &f▎ &7&lINFOS").setLore(
                    "&7Permet de voir les statistiques",
                    "&7des &b" + field.toLowerCase() + "&7.",
                    "",
                    "&8Classement:"
            );

            int i = 1;
            for (QuickLeaderboardData<Integer> value : values) {
                itemBuilder.addLoreLine(" &6&l" + i + "&8■ &7" + value.getPlayerName() + " : &a" + value.getValue());
                i++;
            }

            itemBuilder.addLoreLine(" ");
            itemBuilder.addLoreLine("&f&l» &eContactez un admin en cas d'erreur");

            return itemBuilder.toItemStack();
        }
    }
}
