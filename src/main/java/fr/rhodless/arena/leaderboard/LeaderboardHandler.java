package fr.rhodless.arena.leaderboard;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.config.list.ConfigValues;
import fr.rhodless.arena.mongo.MongoHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class LeaderboardHandler {
    private final Arena arena;

    private final List<QuickLeaderboardData<Integer>> killsLeaderboard = new ArrayList<>();
    private final List<QuickLeaderboardData<Integer>> deathsLeaderboard = new ArrayList<>();
    private final List<QuickLeaderboardData<Integer>> highestKillStreakLeaderboard = new ArrayList<>();

    private final List<ArmorStand> leaderboardHolograms = new ArrayList<>();

    public LeaderboardHandler(Arena arena) {
        this.arena = arena;

        new BukkitRunnable() {
            @Override
            public void run() {
                updateLeaderboard();
            }
        }.runTaskTimer(arena, 0, 10 * 20);
    }

    public void updateLeaderboard() {
        MongoHandler mongoHandler = arena.getMongoHandler();
        mongoHandler.getProfileCollection().getLeaderboard("kills", killsLeaderboard);
        mongoHandler.getProfileCollection().getLeaderboard("deaths", deathsLeaderboard);
        mongoHandler.getProfileCollection().getLeaderboard("highestKillStreak", highestKillStreakLeaderboard);

        Bukkit.getWorld("world").getEntities().stream()
                .filter(entity -> entity instanceof ArmorStand)
                .forEach(Entity::remove);
        this.leaderboardHolograms.clear();

        Bukkit.getScheduler().runTaskLater(arena, () -> this.leaderboardHolograms.addAll(this.createLeaderboardHolograms()), 20);
    }

    private Collection<? extends ArmorStand> createLeaderboardHolograms() {
        List<ArmorStand> armorStands = new ArrayList<>();

        Location center = ((Location) ConfigValues.SPAWN.getValue()).clone().add(0, 3.5, 10);
        armorStands.add(this.createHologram("§6§lClassement des Kills §f▎ §7§lINFOS", center, 0));
        armorStands.add(this.createHologram("§7§m----------------------", center, -0.3F));
        float yChange = -0.3F;
        for (int i = 0; i < 10; i++) {
            if (i >= this.killsLeaderboard.size()) break;

            yChange -= 0.3F;
            QuickLeaderboardData<Integer> data = this.killsLeaderboard.get(i);

            String text = "  §6§l" + (i + 1) + "§8■ §7" + data.getPlayerName() + " : §a" + data.getValue();
            armorStands.add(this.createHologram(text, center, yChange));
        }

        yChange -= 0.3F;
        armorStands.add(this.createHologram("§7§m----------------------", center, yChange));

        return armorStands;
    }

    private ArmorStand createHologram(String text, Location location, float yChange) {
        ArmorStand armorStand = location.getWorld().spawn(location.clone().add(0, yChange, 0), ArmorStand.class);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setMarker(true);

        return armorStand;
    }
}
