package fr.rhodless.arena.scoreboard.impl;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.leaderboard.LeaderboardHandler;
import fr.rhodless.arena.leaderboard.QuickLeaderboardData;
import fr.rhodless.arena.profile.Profile;
import fr.rhodless.arena.scoreboard.CoreScoreboard;
import fr.rhodless.arena.scoreboard.ScoreboardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class CoreScoreboardAdapter implements ScoreboardAdapter {
    @Override
    public String getTitle(Player p0) {
        return "&6&lArena";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> scoreboard = new ArrayList<>();

        Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);

        scoreboard.add(" ");
        scoreboard.add(" &8❘ &7Joueurs : &6" + Bukkit.getOnlinePlayers().size());
        String kit = (profile == null || (profile.getKit() == null && profile.getSelectedKit() == null) ? "&cAucun" : (profile.getKit() == null ? profile.getSelectedKit().getName() : profile.getKit().getName()));
        scoreboard.add(" &8❘ &7Kit : &6" + kit);
        scoreboard.add(" ");
        scoreboard.add(" &8❘ &7Kills : &6" + (profile == null ? "&cChargement..." : profile.getKills()));
        scoreboard.add(" &8❘ &7Morts : &6" + (profile == null ? "&cChargement..." : profile.getDeaths()));
        String highestKillStreak = (profile == null ? "&cChargement..." : profile.getHighestKillStreak() + "");
        scoreboard.add(" &8❘ &7KillStreak : &6" + (profile == null ? "&cChargement..." : profile.getKillStreak() + " &7(" + highestKillStreak + ")"));
        scoreboard.add(" ");
        scoreboard.add(" &8❘ &7Top Kills :");
        LeaderboardHandler leaderboardHandler = Arena.getInstance().getLeaderboardHandler();
        int i = 0;
        for (QuickLeaderboardData<Integer> value : leaderboardHandler.getKillsLeaderboard()) {
            i++;
            if (i > 3) break;
            scoreboard.add("   &8■ &7" + value.getPlayerName() + " : &a" + value.getValue());
        }
        scoreboard.add(" ");
        scoreboard.add(CoreScoreboard.getInstance().getAddressAnimation().getColorAddress());

        return scoreboard;
    }
}
