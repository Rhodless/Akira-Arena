package fr.rhodless.arena.command.parameter.defaults;


import fr.rhodless.arena.command.parameter.PType;
import fr.rhodless.arena.utils.text.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
public class PlayerType implements PType<Player> {
    @Override
    public Player transform(CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
            return ((Player) sender);
        }
        if (!(sender instanceof Player) && (source.equalsIgnoreCase("self") || source.equals(""))) {
            return (null);
        }

        Player player = Bukkit.getPlayer(source);

        if (player == null) {
            if (sender instanceof Player) {
                sender.sendMessage(CC.prefix("&cCe joueur n'est pas connecté."));
            }
            return (null);
        }

        return (player);
    }

    @Override
    public List<String> complete(Player sender, String source) {
        List<String> completions = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (StringUtils.startsWithIgnoreCase(player.getName(), source) && (sender.canSee(player))) {
                completions.add(player.getName());
            }
        }

        return (completions);
    }
}