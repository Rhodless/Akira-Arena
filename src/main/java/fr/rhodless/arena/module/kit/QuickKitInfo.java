package fr.rhodless.arena.module.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class QuickKitInfo {
    private final String name;
    private final ItemStack itemStack;
    private final List<String> description;
    private final Class<? extends ArenaKit> kitClass;
}
