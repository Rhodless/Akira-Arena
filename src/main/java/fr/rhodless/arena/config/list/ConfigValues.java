package fr.rhodless.arena.config.list;

import fr.rhodless.arena.config.ConfigurationFiles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
@Getter
@RequiredArgsConstructor
public enum ConfigValues {
    SPAWN("spawn", new Location(Bukkit.getWorld("world"), 0, 100, 0, 90, 0));

    private final String path;
    private final Object defaultValue;

    @Setter
    private Object value;

    public void saveValue(Object value) {
        this.value = value;

        FileConfiguration configuration = ConfigurationFiles.CONFIG.getConfig();
        configuration.set(path, value);
        ConfigurationFiles.CONFIG.save(configuration);
    }

    public static void load() {
        FileConfiguration configuration = ConfigurationFiles.CONFIG.getConfig();

        for (ConfigValues value : values()) {
            if (configuration.contains(value.getPath())) {
                value.setValue(configuration.get(value.getPath()));
            } else {
                value.saveValue(value.getDefaultValue());
            }
        }
    }
}
