package fr.rhodless.arena.command.parameter.defaults;


import fr.rhodless.arena.command.parameter.PType;
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
public class StringType implements PType<String> {

    public String transform(CommandSender sender, String source) {
        return source;
    }

    public List<String> complete(Player sender,  String source) {
        return (new ArrayList<>());
    }

}