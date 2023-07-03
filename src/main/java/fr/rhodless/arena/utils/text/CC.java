package fr.rhodless.arena.utils.text;

import org.bukkit.ChatColor;

public class CC {
    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String prefix(String message) {
        return translate("&6&lArena &8&l» &f" + message);
    }
}
