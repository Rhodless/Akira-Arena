package fr.rhodless.arena.scoreboard;

import fr.rhodless.menu.api.utils.text.CC;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
@Getter
public class CoreScoreboard {
    @Getter
    private static CoreScoreboard instance;

    private final Plugin plugin;
    private final ScoreboardAdapter adapter;
    private final Map<UUID, ScoreboardBoard> boards;
    private final ScoreboardAnimation.AddressAnimation addressAnimation;

    public CoreScoreboard(final Plugin plugin, final ScoreboardAdapter adapter) {
        if (CoreScoreboard.instance != null) {
            throw new RuntimeException("CoreScoreboard a déjà été lancé !");
        }
        CoreScoreboard.instance = this;
        this.plugin = plugin;
        this.adapter = adapter;
        this.addressAnimation = new ScoreboardAnimation.AddressAnimation("play.edenmc.fr");
        this.boards = new HashMap<>();

        this.setup();
    }

    private void setup() {

        this.plugin.getServer().getPluginManager().registerEvents(new ScoreboardListener(), this.plugin);
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                try {
                    ScoreboardBoard board = this.boards.get(player.getUniqueId());
                    if (board == null)
                        continue;
                    Scoreboard scoreboard = board.getScoreboard();
                    Objective objective = board.getObjective();
                    String title = CC.translate(adapter.getTitle(player));
                    if (!objective.getDisplayName().equals(title))
                        objective.setDisplayName(title);
                    List<String> newLines = this.adapter.getLines(player);
                    if (newLines == null || newLines.isEmpty()) {
                        board.getEntries().forEach(ScoreboardBoardEntry::remove);
                        board.getEntries().clear();
                    } else {
                        Collections.reverse(newLines);
                        if (board.getEntries().size() > newLines.size())
                            for (int j = newLines.size(); j < board.getEntries().size(); j++) {
                                ScoreboardBoardEntry entry = board.getEntryAtPosition(j);
                                if (entry != null)
                                    entry.remove();
                            }
                        for (int i = 0; i < newLines.size(); i++) {
                            ScoreboardBoardEntry entry = board.getEntryAtPosition(i);
                            String line = ChatColor.translateAlternateColorCodes('&', newLines.get(i));
                            if (entry == null)
                                entry = new ScoreboardBoardEntry(board, line);
                            entry.setText(line);
                            entry.setup();
                            entry.send(i);
                        }
                    }
                    player.setScoreboard(scoreboard);
                } catch (Exception ignored) {
                }
            }

        }, 1L, 2L);
    }
}
