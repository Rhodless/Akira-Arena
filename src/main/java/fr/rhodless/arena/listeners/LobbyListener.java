package fr.rhodless.arena.listeners;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.profile.impl.CoreProfile;
import fr.rhodless.arena.profile.state.ProfileState;
import fr.rhodless.arena.utils.nms.Title;
import fr.rhodless.arena.utils.text.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

@RequiredArgsConstructor
public class LobbyListener implements Listener {
    private final Arena arena;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);

        Title.sendActionBar(CC.prefix("&a" + player.getName() + " &fa rejoint le serveur"));

        CoreProfile.refreshLobbyHotBar(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            arena.getItemHandler().getItems().stream()
                    .filter(item -> item.getItemStack().isSimilar(event.getItem()))
                    .findFirst().ifPresent(item -> item.onClick(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            arena.getItemHandler().getItems().stream()
                    .filter(item -> item.getItemStack().isSimilar(event.getCurrentItem()))
                    .findFirst().ifPresent(item -> item.onClick(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY && !profile.isEditing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        Profile profile = arena.getProfileHandler().getProfile(player);

        if (profile == null || profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(true);
        }
    }
}
