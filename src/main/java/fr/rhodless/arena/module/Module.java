package fr.rhodless.arena.module;

import fr.rhodless.arena.module.kit.ArenaKit;
import fr.rhodless.arena.module.kit.QuickKitInfo;

import java.util.List;

public abstract class Module {
    private final String name;

    public Module(String name) {
        this.name = name;
    }

    public abstract List<QuickKitInfo> getKits();
}
