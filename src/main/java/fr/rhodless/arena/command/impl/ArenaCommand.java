package fr.rhodless.arena.command.impl;

import fr.rhodless.arena.command.annotations.MainCommand;
import fr.rhodless.arena.command.annotations.SubCommand;
import fr.rhodless.arena.config.list.ConfigInventories;
import fr.rhodless.arena.config.list.ConfigLists;
import fr.rhodless.arena.config.list.ConfigValues;
import fr.rhodless.arena.utils.text.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@MainCommand(names = {"arena", "akira-arena"}, description = "Commandes de l'arena", helpCommand = "help")
public class ArenaCommand {
    @SubCommand(names = "setspawn", description = "Définir le point de spawn", permission = "akira.arena.setspawn")
    public void setSpawn(Player sender) {
        Location location = sender.getLocation().clone();

        ConfigValues.SPAWN.saveValue(location);
        sender.sendMessage(CC.prefix("&cLe point de spawn a été défini à &4" + location.getBlockX() + "&c, &4" + location.getBlockY() + "&c, &4" + location.getBlockZ() + "&c."));
    }

    @SubCommand(names = "getinventory", description = "Obtenir l'inventaire par défaut", permission = "akira.arena.getinventory")
    public void getInventory(Player sender) {
        sender.getInventory().setContents(ConfigInventories.DEFAULT_INVENTORY.getValue());
        sender.getInventory().setArmorContents(ConfigInventories.DEFAULT_ARMOR.getValue());
        sender.sendMessage(CC.prefix("&aL'inventaire par défaut a été ajouté à votre inventaire."));
    }

    @SubCommand(names = "setinventory", description = "Définir l'inventaire par défaut", permission = "akira.arena.setinventory")
    public void setInventory(Player sender) {
        ConfigInventories.DEFAULT_INVENTORY.save(sender.getInventory().getContents());
        ConfigInventories.DEFAULT_ARMOR.save(sender.getInventory().getArmorContents());
        sender.sendMessage(CC.prefix("&aL'inventaire par défaut a été défini."));
    }

    @SubCommand(names = "addtppoint", description = "Ajouter un point de téléportation", permission = "akira.arena.addtppoint")
    public void addTpPoint(Player sender) {
        Location location = sender.getLocation().clone();

        List<Location> locations = (List<Location>) ConfigLists.TP_SPAWNS.getValue();
        locations.add(location);
        ConfigLists.TP_SPAWNS.saveValue(locations);

        sender.sendMessage(CC.prefix("&cVous avez ajouté un point de téléportation en &4" + location.getBlockX() + "&c, &4" + location.getBlockY() + "&c, &4" + location.getBlockZ() + "&c."));
    }
}
