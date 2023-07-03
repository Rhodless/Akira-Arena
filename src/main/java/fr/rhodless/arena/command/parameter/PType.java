package fr.rhodless.arena.command.parameter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public interface PType<T> {

    T transform(CommandSender sender, String string);

    List<String> complete(Player sender, String string);

}
