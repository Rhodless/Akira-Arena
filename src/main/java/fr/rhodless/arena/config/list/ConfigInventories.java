package fr.rhodless.arena.config.list;

import fr.rhodless.arena.config.ConfigurationFiles;
import fr.rhodless.arena.utils.inventory.Base64Inventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public enum ConfigInventories {
    DEFAULT_INVENTORY("default-inventory", null),
    DEFAULT_ARMOR("default-armor", null);

    private final String path;
    private final String defaultValue;

    @Setter
    private ItemStack[] value;

    public void save(ItemStack[] value) {
        this.value = value;

        FileConfiguration configuration = ConfigurationFiles.CONFIG.getConfig();
        configuration.set(path, Base64Inventory.itemStackArrayToBase64(value));
        ConfigurationFiles.CONFIG.save(configuration);
    }

    public static void load() {
        FileConfiguration configuration = ConfigurationFiles.CONFIG.getConfig();

        for (ConfigInventories value : values()) {
            if (configuration.contains(value.getPath())) {
                value.setValue(Base64Inventory.itemStackArrayFromBase64(configuration.getString(value.getPath())));
            } else {
                configuration.set(value.getPath(), value.getDefaultValue());
                ConfigurationFiles.CONFIG.save(configuration);

                value.setValue(Base64Inventory.itemStackArrayFromBase64(value.getDefaultValue()));
            }
        }
    }
}
