package fr.rhodless.arena;

import fr.rhodless.arena.command.CommandHandler;
import fr.rhodless.arena.command.impl.ArenaCommand;
import fr.rhodless.arena.command.impl.SaveCommand;
import fr.rhodless.arena.config.ConfigurationHandler;
import fr.rhodless.arena.item.ItemHandler;
import fr.rhodless.arena.leaderboard.LeaderboardHandler;
import fr.rhodless.arena.listeners.GameListener;
import fr.rhodless.arena.listeners.LobbyListener;
import fr.rhodless.arena.listeners.PowerListener;
import fr.rhodless.arena.listeners.ProfileListener;
import fr.rhodless.arena.module.ModuleHandler;
import fr.rhodless.arena.mongo.MongoHandler;
import fr.rhodless.arena.profile.ProfileHandler;
import fr.rhodless.arena.scoreboard.CoreScoreboard;
import fr.rhodless.arena.scoreboard.impl.CoreScoreboardAdapter;
import fr.rhodless.menu.api.MenuHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Arena extends JavaPlugin {
    @Getter
    private static Arena instance;

    private MongoHandler mongoHandler;
    private ProfileHandler profileHandler;
    private ItemHandler itemHandler;
    private ModuleHandler moduleHandler;
    private ConfigurationHandler configurationHandler;
    private CommandHandler commandHandler;
    private MenuHandler menuHandler;
    private LeaderboardHandler leaderboardHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.registerWorlds();

        this.mongoHandler = new MongoHandler(this, "arena", "mongodb://localhost:27017");
        this.profileHandler = new ProfileHandler(this);
        this.itemHandler = new ItemHandler(this);
        this.moduleHandler = new ModuleHandler(this);
        this.configurationHandler = new ConfigurationHandler(this);
        this.commandHandler = new CommandHandler(this);
        this.menuHandler = new MenuHandler(this);
        this.leaderboardHandler = new LeaderboardHandler(this);

        new CoreScoreboard(this, new CoreScoreboardAdapter());

        this.registerListeners();
        this.registerCommands();
    }

    private void registerWorlds() {
        new WorldCreator("arena").createWorld();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.getWorld("arena").getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
            Bukkit.getWorld("world").getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
        }, 40);
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new ProfileListener(this), this);
        pluginManager.registerEvents(new LobbyListener(this), this);
        pluginManager.registerEvents(new PowerListener(this), this);
        pluginManager.registerEvents(new GameListener(this), this);
    }

    private void registerCommands() {
        this.commandHandler.registerCommandWithSubCommands(new ArenaCommand());
        this.commandHandler.registerCommands(new SaveCommand());
    }
}
