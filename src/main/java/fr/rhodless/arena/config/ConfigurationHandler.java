package fr.rhodless.arena.config;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.config.list.ConfigInventories;
import fr.rhodless.arena.config.list.ConfigLists;
import fr.rhodless.arena.config.list.ConfigValues;
import lombok.Getter;

@Getter
public class ConfigurationHandler {
    private final Arena arena;

    public ConfigurationHandler(Arena arena) {
        this.arena = arena;

        this.registerConfigs();
    }

    private void registerConfigs() {
        ConfigValues.load();
        ConfigInventories.load();
        ConfigLists.load();
    }
}
