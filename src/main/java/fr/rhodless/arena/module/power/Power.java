package fr.rhodless.arena.module.power;

import lombok.Getter;
import lombok.Setter;
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

@Getter
@Setter
public abstract class Power {
    private final List<RestrictionType> restrictions = new ArrayList<>();
    private final Cooldown cooldown = new Cooldown(this.getPowerName(), null);
    private final int cooldownAmount;

    public Power(int cooldown, int maxUses) {
        this.cooldownAmount = cooldown;

        if (cooldownAmount != -1) {
            this.restrictions.add(RestrictionType.COOLDOWN);
        }
    }

    public abstract String getPowerName();

    public abstract boolean onEnable(Player player);

    public boolean hasRestriction(RestrictionType restriction) {
        return getRestrictions().contains(restriction);
    }

    public boolean usePower(Player player) {
        if (hasRestriction(RestrictionType.COOLDOWN) && cooldown.isCooldown(player)) {
            return false;
        }

        if (!this.onEnable(player)) {
            return false;
        }

        if (this.hasRestriction(RestrictionType.COOLDOWN)) {
            cooldown.setSeconds(cooldownAmount);
            cooldown.task();
        }

        return true;
    }
}
