package fr.rhodless.arena.item.list;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.config.list.ConfigLists;
import fr.rhodless.arena.item.Item;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.profile.state.ProfileState;
import fr.rhodless.arena.utils.text.CC;
import fr.rhodless.menu.api.utils.Heads;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayItem extends Item {
    @Override
    public ItemStack getItemStack() {
        return format(Heads.VALIDATE.toItemStack(), "Jouer").toItemStack();
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public void onClick(Player player) {
        Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
        if (profile == null || profile.getSelectedKit() == null) {
            player.sendMessage(CC.prefix("&cVous devez avoir choisi un kit pour jouer."));
            return;
        }

        if (profile.getState() == ProfileState.PLAYING) {
            player.sendMessage(CC.prefix("&cVous êtes déjà en partie."));
            return;
        }

        try {
            profile.setKit(profile.getSelectedKit().getKitClass().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            player.sendMessage(CC.prefix("&cUne erreur est survenue, veuillez contacter un administrateur"));
            throw new RuntimeException(e);
        }

        profile.setState(ProfileState.PLAYING);
        profile.refreshGameHotBar();
        profile.getKitHistory().add(profile.getKit().getName());

        List<Location> possibleLocations = (List<Location>) ConfigLists.TP_SPAWNS.getValue();
        Location location = possibleLocations.get((int) (Math.random() * possibleLocations.size()));

        player.teleport(location);
    }
}
