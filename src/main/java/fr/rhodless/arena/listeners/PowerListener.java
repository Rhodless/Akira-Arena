package fr.rhodless.arena.listeners;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.module.kit.ArenaKit;
import fr.rhodless.arena.module.power.Power;
import fr.rhodless.arena.module.power.defaults.*;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.utils.text.CC;
import fr.rhodless.menu.api.utils.nms.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
@RequiredArgsConstructor
public class PowerListener implements Listener {
    private final Arena arena;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);
        ArenaKit kit = profile.getKit();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || !itemStack.hasItemMeta()) return;
        if (kit == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (Power power : kit.getPowers()) {
                if (!(power instanceof RightClickPower)) {
                    continue;
                }

                RightClickPower rightClickPower = (RightClickPower) power;
                if (rightClickPower.isSimilar(itemStack)) {
                    rightClickPower.usePower(event.getPlayer());
                }
            }
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            for (Power power : kit.getPowers()) {
                if (!(power instanceof LeftClickPower)) {
                    continue;
                }

                LeftClickPower leftClickPower = (LeftClickPower) power;

                if (leftClickPower.isSimilar(itemStack)) {
                    leftClickPower.usePower(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);
        ArenaKit kit = profile.getKit();

        if (kit == null) return;
        for (Power power : kit.getPowers()) {
            if (!(power instanceof BlockPlacePower)) {
                continue;
            }

            BlockPlacePower blockPlacePower = (BlockPlacePower) power;
            if (blockPlacePower.isSimilar(player.getItemInHand())) {
                blockPlacePower.setBlock(event.getBlock());
                event.setCancelled(true);

                blockPlacePower.usePower(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Profile profile = arena.getProfileHandler().getProfile(player);
        ArenaKit kit = profile.getKit();

        if (kit == null) return;
        for (Power power : kit.getPowers()) {
            if (!(power instanceof PlayerDamagePower)) continue;

            PlayerDamagePower playerDamagePower = (PlayerDamagePower) power;
            if (playerDamagePower.isSimilar(player.getItemInHand())) {
                playerDamagePower.setTarget((Player) event.getEntity());
                playerDamagePower.usePower(player);
            }
        }
    }

    @EventHandler
    public void onInteractAtPlayer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = arena.getProfileHandler().getProfile(player);
        ArenaKit kit = profile.getKit();

        if (kit == null) return;
        for (Power power : kit.getPowers()) {

            if (event.getAction().name().contains("RIGHT")) {
                if (!(power instanceof RightClickPlayerPower)) continue;

                RightClickPlayerPower rightClickPlayerPower = (RightClickPlayerPower) power;
                boolean found = false;

                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!target.getWorld().getName().equals(player.getWorld().getName())) continue;
                    if (target.getLocation().distance(player.getLocation()) > rightClickPlayerPower.getDistance())
                        continue;
                    if (!ReflectionUtils.getLookingAt(player, target)) continue;

                    if (rightClickPlayerPower.isSimilar(event.getItem())) {
                        Profile targetProfile = arena.getProfileHandler().getProfile(target);
                        if (targetProfile.isResistantFromTargetPowers()) {
                            player.sendMessage(CC.prefix("&cVous ne pouvez pas utiliser ce pouvoir sur ce joueur."));
                            return;
                        }

                        found = true;
                        rightClickPlayerPower.setTarget(target);
                        rightClickPlayerPower.usePower(event.getPlayer());
                    }
                    break;
                }

                if (!found) {
                    if (rightClickPlayerPower.isSimilar(event.getItem())) {
                        player.sendMessage(CC.prefix("&cVous ne ciblez aucun joueur à proximité."));
                    }
                }
            }

            if (event.getAction().name().contains("LEFT")) {
                if (!(power instanceof LeftClickPlayerPower)) continue;

                LeftClickPlayerPower leftClickPlayerPower = (LeftClickPlayerPower) power;
                boolean found = false;

                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!target.getWorld().getName().equals(player.getWorld().getName())) continue;
                    if (target.getLocation().distance(player.getLocation()) > leftClickPlayerPower.getDistance())
                        continue;
                    if (!ReflectionUtils.getLookingAt(player, target)) continue;

                    if (leftClickPlayerPower.isSimilar(event.getItem())) {
                        Profile targetProfile = arena.getProfileHandler().getProfile(target);
                        if (targetProfile.isResistantFromTargetPowers()) {
                            player.sendMessage(CC.prefix("&cVous ne pouvez pas utiliser ce pouvoir sur ce joueur."));
                            return;
                        }

                        found = true;
                        leftClickPlayerPower.setTarget(target);
                        leftClickPlayerPower.usePower(event.getPlayer());
                    }
                    break;
                }

                if (!found) {
                    if (leftClickPlayerPower.isSimilar(event.getItem())) {
                        player.sendMessage(CC.prefix("&cVous ne ciblez aucun joueur à proximité."));
                    }
                }
            }
        }
    }
}
