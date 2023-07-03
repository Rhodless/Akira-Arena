package fr.rhodless.arena.listeners;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.profile.impl.CoreProfile;
import fr.rhodless.arena.profile.state.ProfileState;
import fr.rhodless.arena.utils.text.CC;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameListener implements Listener {
    private final Arena arena;
    private final Map<Location, Long> placedBlocks = new ConcurrentHashMap<>();

    public GameListener(Arena arena) {
        this.arena = arena;

        new BukkitRunnable() {
            @Override
            public void run() {
                placedBlocks.forEach((location, aLong) -> {
                    Bukkit.broadcastMessage("Block type is: " + location.getBlock().getType().name());
                });

                placedBlocks.forEach((location, time) -> {
                    if (System.currentTimeMillis() - time > 15000) {
                        placedBlocks.remove(location);
                        location.getBlock().setType(Material.AIR);

                        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), 0);
                        Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().equals(location.getWorld())).filter(player -> player.getLocation().distance(location) <= 30).forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
                    }
                });
            }
        }.runTaskTimer(arena, 0L, 20L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        event.getDrops().clear();
        event.setDeathMessage(null);
        Bukkit.broadcastMessage(CC.prefix("&6" + player.getName() + " &f" + (killer == null ? "est mort tout seul" : "a été tué par &c" + killer.getName())));

        Profile playerProfile = arena.getProfileHandler().getProfile(player);
        playerProfile.setKillStreak(0);
        playerProfile.setDeaths(playerProfile.getDeaths() + 1);
        arena.getProfileHandler().saveProfile(playerProfile);

        if (killer != null) {
            Profile killerProfile = arena.getProfileHandler().getProfile(killer);
            killerProfile.setKillStreak(killerProfile.getKillStreak() + 1);
            killerProfile.setKills(killerProfile.getKills() + 1);

            if (killerProfile.getKillStreak() > killerProfile.getHighestKillStreak()) {
                killerProfile.setHighestKillStreak(killerProfile.getKillStreak());
            }
            arena.getProfileHandler().saveProfile(killerProfile);

            int slotApples = player.getInventory().first(Material.GOLDEN_APPLE);
            if (slotApples != -1) {
                killer.getInventory().addItem(player.getInventory().getItem(slotApples));
            }

            int slotArrows = player.getInventory().first(Material.ARROW);
            if (slotArrows != -1) {
                killer.getInventory().addItem(player.getInventory().getItem(slotArrows));
            }

            killer.removePotionEffect(PotionEffectType.ABSORPTION);
            killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2 * 20 * 60, 0, false, false));
            killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 2, false, false));
        }

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        Bukkit.getScheduler().runTaskLater(Arena.getInstance(), () -> {
            player.spigot().respawn();
            playerProfile.setState(ProfileState.LOBBY);
            CoreProfile.refreshLobbyHotBar(player);
            player.getInventory().setHeldItemSlot(2);
        }, 20);
    }

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) {
            return;
        }

        Arrow arrow = (Arrow) event.getDamager();
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) arrow.getShooter();
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player target = (Player) event.getEntity();

        float health = (float) (target.getHealth() - (float) event.getFinalDamage());
        if (health <= 0) {
            return;
        }
        String format = new DecimalFormat("#.#").format(health);

        float absorptionHearts = ((CraftPlayer) target).getHandle().getAbsorptionHearts() / 2;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        player.sendMessage(CC.prefix("&c" + target.getName() + " &fest maintenant à &c" + format + " ❤ &8(&e" + decimalFormat.format(absorptionHearts) + " ❤&8)"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        placedBlocks.remove(block.getLocation());

        placedBlocks.put(block.getLocation(), System.currentTimeMillis());
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        Block block = event.getBlock();

        placedBlocks.remove(block.getLocation());

        placedBlocks.put(block.getLocation(), System.currentTimeMillis());
    }

    @EventHandler
    public void onBlockForm(BlockFromToEvent event) {
        Block block = event.getToBlock();

        placedBlocks.remove(block.getLocation());

        placedBlocks.put(block.getLocation(), System.currentTimeMillis());
    }

    @EventHandler
    public void onBlockForm(BlockSpreadEvent event) {
        Block block = event.getBlock();

        placedBlocks.remove(block.getLocation());

        placedBlocks.put(block.getLocation(), System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        event.setCancelled(true);
        if (placedBlocks.containsKey(block.getLocation())) {
            placedBlocks.remove(block.getLocation());

            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onLand(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        placedBlocks.put(block.getLocation(), System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        placedBlocks.remove(block.getLocation());
    }
}
