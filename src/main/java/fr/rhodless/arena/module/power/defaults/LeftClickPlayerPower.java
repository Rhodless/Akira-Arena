package fr.rhodless.arena.module.power.defaults;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */

public abstract class LeftClickPlayerPower extends ClickPlayerPower {
    public LeftClickPlayerPower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }
}
