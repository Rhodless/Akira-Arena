package fr.rhodless.arena.module.power;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.utils.text.CC;
import fr.rhodless.arena.utils.time.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
public class Cooldown {
    private final String name;
    private BukkitTask runnable;
    private int seconds;
    private final Consumer<Player> consumer;

    public Cooldown(String name, @Nullable Consumer<Player> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public void task() {
        this.task(null);
    }

    public void task(@Nullable Player player) {
        if (runnable != null) runnable.cancel();

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (seconds > 0) {
                    onSecond();
                    return;
                }
                cancel();

                if (player == null) return;
                onEnd(player);
            }
        }.runTaskTimer(Arena.getInstance(), 0, 20);
    }

    public void onSecond() {
        this.seconds--;
    }

    public String getCooldownMessage() {
        return CC.prefix("&cVous ne pouvez pas utiliser cette capacité. Elle sera de nouveau disponible dans " + TimeUtil.getReallyNiceTime2(this.getSeconds() * 1000L));
    }

    public boolean isCooldown(Player player) {
        if (this.getSeconds() <= 0) return false;

        player.sendMessage(getCooldownMessage());

        return true;
    }

    public boolean isCooldownNoMessage() {
        return this.getSeconds() > 0;
    }

    public void onEnd(Player player) {
        if (this.consumer != null) this.consumer.accept(player);
    }

}

