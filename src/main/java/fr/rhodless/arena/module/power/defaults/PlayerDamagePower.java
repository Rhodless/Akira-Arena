package fr.rhodless.arena.module.power.defaults;

import org.bukkit.entity.Player;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */

public abstract class PlayerDamagePower extends ItemPower {
    private Player target;

    public PlayerDamagePower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }

    /**
     * Permet de récupérer le joueur ciblé
     *
     * @return le joueur ciblé
     */
    public Player getTarget() {
        return target;
    }

    /**
     * Permet de définir le joueur ciblé
     *
     * @param target le joueur ciblé
     */
    public void setTarget(Player target) {
        this.target = target;
    }
}
