package fr.rhodless.arena.profile.impl;

import com.google.gson.annotations.SerializedName;
import fr.rhodless.arena.Arena;
import fr.rhodless.arena.config.list.ConfigInventories;
import fr.rhodless.arena.config.list.ConfigValues;
import fr.rhodless.arena.item.Item;
import fr.rhodless.arena.module.kit.ArenaKit;
import fr.rhodless.arena.module.kit.QuickKitInfo;
import fr.rhodless.arena.module.power.Power;
import fr.rhodless.arena.module.power.defaults.ItemPower;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.profile.state.ProfileState;
import fr.rhodless.arena.utils.inventory.Base64Inventory;
import fr.rhodless.menu.api.utils.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoreProfile implements Profile {
    @SerializedName("_id")
    private UUID uniqueId;
    private String displayName;
    private int kills = 0;
    private int deaths = 0;
    private transient int killStreak = 0;
    private int highestKillStreak = 0;
    private transient String currentKit;
    private List<String> kitHistory = new ArrayList<>();
    private String base64Inventory = null;
    private transient ProfileState state = ProfileState.LOBBY;
    private transient ArenaKit kit = null;
    private transient boolean editing = false;
    private transient QuickKitInfo selectedKit = null;
    private transient boolean resistantFromTargetPowers = false;

    public CoreProfile(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.displayName = name;
    }

    @Override
    @Nullable
    public ItemStack[] getSavedInventory() {
        if (this.base64Inventory == null) {
            return null;
        }

        return Base64Inventory.itemStackArrayFromBase64(base64Inventory);
    }

    @Override
    public void setSavedInventory(ItemStack[] savedInventory) {
        this.base64Inventory = Base64Inventory.itemStackArrayToBase64(savedInventory);
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUniqueId());
    }

    public static void resetPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.2f);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }

    public static void refreshLobbyHotBar(Player player) {
        resetPlayer(player);

        for (Item item : Arena.getInstance().getItemHandler().getItems()) {
            player.getInventory().setItem(item.getSlot(), item.getItemStack());
        }

        player.teleport((Location) ConfigValues.SPAWN.getValue());
    }

    @Override
    public void refreshGameHotBar() {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }

        resetPlayer(player);

        if (this.getSavedInventory() == null) {
            player.getInventory().setContents(ConfigInventories.DEFAULT_INVENTORY.getValue());
        } else {
            player.getInventory().setContents(this.getSavedInventory());
        }
        player.getInventory().setArmorContents(ConfigInventories.DEFAULT_ARMOR.getValue());

        ArenaKit kit = this.getKit();
        if (kit == null) {
            return;
        }

        for (Power power : kit.getPowers()) {
            if (power instanceof ItemPower) {
                ItemPower itemPower = (ItemPower) power;
                if (itemPower.shouldGiveAtDistribution()) {
                    ItemBuilder itemBuilder = new ItemBuilder(((ItemPower) power).getItemStack());
                    itemBuilder.setLore(kit.getDescription());

                    player.getInventory().addItem(itemBuilder.toItemStack());
                }
            }
        }
        kit.onDistribute(player);
        Bukkit.getPluginManager().registerEvents(kit, Arena.getInstance());
    }
}
