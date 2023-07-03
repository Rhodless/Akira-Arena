package fr.rhodless.arena.item;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.item.list.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemHandler {
    private final Arena arena;

    private final List<Item> items = new ArrayList<>();

    public ItemHandler(Arena arena) {
        this.arena = arena;

        this.registerItems();
    }

    private void registerItems() {
        this.items.add(new ChangeKitItem());
        this.items.add(new ChangeInventoryItem());
        this.items.add(new StatistiquesItem());
        this.items.add(new PlayItem());
        this.items.add(new LeaderboardsItem());
    }
}
