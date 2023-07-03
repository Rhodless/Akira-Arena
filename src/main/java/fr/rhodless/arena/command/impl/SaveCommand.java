package fr.rhodless.arena.command.impl;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.command.annotations.Command;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.profile.impl.CoreProfile;
import fr.rhodless.arena.utils.text.CC;
import org.bukkit.entity.Player;

public class SaveCommand {
    @Command(names = "save", description = "Sauvegarder son kit")
    public void saveKit(Player player) {
        Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
        if (!profile.isEditing()) {
            player.sendMessage(CC.prefix("&cVous n'êtes pas en train d'éditer votre kit."));
            return;
        }

        profile.setSavedInventory(player.getInventory().getContents());
        profile.setEditing(false);

        CoreProfile.resetPlayer(player);
        CoreProfile.refreshLobbyHotBar(player);
    }
}
