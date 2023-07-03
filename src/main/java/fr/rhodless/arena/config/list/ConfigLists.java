package fr.rhodless.arena.config.list;

import fr.rhodless.arena.config.ConfigurationFiles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ConfigLists {
    TP_SPAWNS("arena-spawns", new ArrayList<>(Collections.singletonList(new Location(Bukkit.getWorld("arena"), 0, 100, 0))));

    private final String path;
    private final List<?> defaultValue;

    @Setter
    private List<?> value;

    public void saveValue(List<?> value) {
        this.value = value;

        FileConfiguration configuration = ConfigurationFiles.CONFIG.getConfig();
        configuration.set(path, value);
        ConfigurationFiles.CONFIG.save(configuration);
    }

    public static void load() {
        FileConfiguration configuration = ConfigurationFiles.CONFIG.getConfig();

        for (ConfigLists value : values()) {
            if (configuration.contains(value.getPath())) {
                value.setValue(configuration.getList(value.getPath()));
            } else {
                value.saveValue(value.getDefaultValue());
            }
        }
    }
}
