package fr.rhodless.arena.command.parameter.defaults;


import fr.rhodless.arena.command.parameter.PType;
import fr.rhodless.arena.utils.text.CC;
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
public class DoubleType implements PType<Double> {

    @Override
    public Double transform(CommandSender sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(CC.prefix("&cCe nombre n'est pas valide."));
            return (null);
        }

        try {
            double parsed = Double.parseDouble(source);

            if (Double.isNaN(parsed) || !Double.isFinite(parsed)) {
                sender.sendMessage(CC.prefix("&cCe nombre n'est pas valide."));
                return (null);
            }

            return (parsed);
        } catch (NumberFormatException exception) {
            if (sender instanceof Player)
                sender.sendMessage(CC.prefix("&cCe nombre n'est pas valide."));
            return (null);
        }
    }

    @Override
    public List<String> complete(Player sender, String source) {
        return (new ArrayList<>());
    }

}