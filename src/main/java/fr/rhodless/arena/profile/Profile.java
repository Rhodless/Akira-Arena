package fr.rhodless.arena.profile;

import fr.rhodless.arena.module.kit.ArenaKit;
import fr.rhodless.arena.module.kit.QuickKitInfo;
import fr.rhodless.arena.profile.state.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface Profile {
    UUID getUniqueId();

    String getDisplayName();

    void setDisplayName(String displayName);

    int getKills();

    void setKills(int kills);

    int getDeaths();

    void setDeaths(int deaths);

    int getKillStreak();

    void setKillStreak(int killStreak);

    int getHighestKillStreak();

    void setHighestKillStreak(int highestKillStreak);

    ItemStack[] getSavedInventory();

    void setSavedInventory(ItemStack[] savedInventory);

    ProfileState getState();

    void setState(ProfileState state);

    ArenaKit getKit();

    void setKit(ArenaKit kit);

    QuickKitInfo getSelectedKit();

    void setSelectedKit(QuickKitInfo selectedKit);

    @Nullable
    Player getPlayer();

    void refreshGameHotBar();

    boolean isEditing();

    void setEditing(boolean editing);

    List<String> getKitHistory();

    void setKitHistory(List<String> kitHistory);

    boolean isResistantFromTargetPowers();

    void setResistantFromTargetPowers(boolean resistantFromTargetPowers);
}
