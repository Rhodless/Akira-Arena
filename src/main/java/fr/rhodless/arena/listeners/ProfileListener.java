package fr.rhodless.arena.listeners;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.profile.Profile;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ProfileListener implements Listener {
    private final Arena arena;

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        arena.getProfileHandler().getProfile(event.getUniqueId(), event.getName(), true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Profile profile = arena.getProfileHandler().getProfile(event.getPlayer());

        event.setQuitMessage(null);
        arena.getProfileHandler().saveProfile(profile);
        arena.getProfileHandler().unregisterPlayer(event.getPlayer());
    }
}
