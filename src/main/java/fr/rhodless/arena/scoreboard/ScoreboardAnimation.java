package fr.rhodless.arena.scoreboard;

import fr.rhodless.menu.api.utils.text.CC;
import org.bukkit.ChatColor;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class ScoreboardAnimation {
    public static class AddressAnimation {
        private final String ip;
        private int ipCharIndex;
        private int cooldown;

        public AddressAnimation(String ip) {
            this.ip = ip;
        }

        public String getColorAddress() {
            String mColor = CC.translate(ChatColor.GOLD.toString());
            if (cooldown > 0) {
                cooldown--;
                return mColor + ip;
            }

            StringBuilder formattedIp = new StringBuilder();

            if (ipCharIndex > 0) {
                formattedIp.append(ip, 0, ipCharIndex - 1);
                formattedIp.append(ChatColor.GRAY).append(ip.charAt(ipCharIndex - 1));
            } else {
                formattedIp.append(ip, 0, ipCharIndex);
            }

            formattedIp.append(ChatColor.WHITE).append(ip.charAt(ipCharIndex));

            if (ipCharIndex + 1 < ip.length()) {
                formattedIp.append(ip.charAt(ipCharIndex + 1));

                if (ipCharIndex + 2 < ip.length())
                    formattedIp.append(mColor).append(ip.substring(ipCharIndex + 2));

                ipCharIndex++;
            } else {
                ipCharIndex = 0;
                cooldown = 30;
            }

            return mColor + formattedIp;
        }
    }
}
