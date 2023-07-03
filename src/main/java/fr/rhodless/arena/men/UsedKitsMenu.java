package fr.rhodless.arena.men;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.menu.StatistiquesMenu;
import fr.rhodless.arena.module.kit.QuickKitInfo;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.menu.api.Button;
import fr.rhodless.menu.api.Menu;
import fr.rhodless.menu.api.pagination.PaginatedMenu;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UsedKitsMenu extends PaginatedMenu {
    private final StatistiquesMenu statistiquesMenu;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Kit utilisés";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
        for (String s : profile.getKitHistory()) {
            QuickKitInfo arenaKit = Arena.getInstance().getModuleHandler().getModule().getKits().stream()
                    .filter(quickKitInfo -> quickKitInfo.getName().equalsIgnoreCase(s))
                    .findFirst().orElse(null);
            if (arenaKit == null) {
                continue;
            }

            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(arenaKit.getItemStack()).setName("&6&l" + arenaKit.getName()).setLore(arenaKit.getDescription()).toItemStack();
                }
            });
        }

        return buttons;
    }

    @Override
    public Menu backButton() {
        return statistiquesMenu;
    }
}
