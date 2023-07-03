package fr.rhodless.arena.command.parameter.defaults;

import fr.rhodless.arena.command.parameter.PType;
import fr.rhodless.arena.utils.text.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
public class WorldType implements PType<World> {
    @Override
    public World transform(CommandSender sender, String source) {
        World world = Bukkit.getWorld(source);

        if (world == null) {
            sender.sendMessage(CC.prefix("&cCe monde n'existe pas."));
            return (null);
        }

        return (world);
    }

    @Override
    public List<String> complete(Player sender, String source) {
        List<String> completions = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            if (StringUtils.startsWithIgnoreCase(world.getName(), source)) {
                completions.add(world.getName());
            }
        }

        return (completions);
    }

}