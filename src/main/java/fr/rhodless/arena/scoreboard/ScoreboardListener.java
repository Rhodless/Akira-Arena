package fr.rhodless.arena.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class ScoreboardListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        CoreScoreboard.getInstance().getBoards().put(event.getPlayer().getUniqueId(), new ScoreboardBoard(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        CoreScoreboard.getInstance().getBoards().remove(event.getPlayer().getUniqueId());
    }
}
