package fr.rhodless.arena.module.kit;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.module.power.Power;
import fr.rhodless.arena.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class ArenaKit implements Listener {
    private final String name;
    private final ItemStack itemStack;
    private final List<Power> powers;
    private final List<String> description;

    public ArenaKit(String name, ItemStack itemStack, List<String> description, Power... powers) {
        this.name = name;
        this.itemStack = itemStack;
        this.description = description;
        this.powers = new ArrayList<>(Arrays.asList(powers));
    }

    public abstract void onDistribute(Player player);

    protected boolean hasKit(Player player) {
        Profile profile = Arena.getInstance().getProfileHandler().getProfile(player);
        if (profile == null || profile.getKit() == null) {
            return false;
        } else {
            return profile.getKit().equals(this);
        }
    }
}
