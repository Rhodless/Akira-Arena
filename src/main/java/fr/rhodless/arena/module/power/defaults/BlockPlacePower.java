package fr.rhodless.arena.module.power.defaults;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */

import org.bukkit.block.Block;

public abstract class BlockPlacePower extends ItemPower {
    private Block block;

    public BlockPlacePower(int cooldown, int maxUses) {
        super(cooldown, maxUses);
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
