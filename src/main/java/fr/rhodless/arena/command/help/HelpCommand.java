package fr.rhodless.arena.command.help;

import com.google.common.base.Strings;
import fr.rhodless.arena.command.annotations.SubCommand;
import fr.rhodless.menu.api.utils.text.CC;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class HelpCommand {
    public static final int ELEMENTS_PER_PAGE = 6;

    private final String description;
    private final List<SubCommand> subCommands;

    public int getTotalPages() {
        return (int) Math.ceil((double) subCommands.size() / ELEMENTS_PER_PAGE);
    }

    public void display(Player player, String command, int page) {
        List<SubCommand> subCommands = this.subCommands.subList((page - 1) * ELEMENTS_PER_PAGE, Math.min(page * ELEMENTS_PER_PAGE, this.subCommands.size()));

        player.sendMessage(getLineWithText(description));

        for (SubCommand subCommand : subCommands) {
            player.sendMessage(CC.translate("&8» &6/&e" + command + " &6" + subCommand.names()[0] + " &8- &7" + subCommand.description()));
        }

        player.sendMessage(" ");
        TextComponent textComponent = new TextComponent(CC.translate(" &7Page &a" + page + "&8/&a" + getTotalPages() + " &8- "));

        TextComponent previous = new TextComponent();
        if (page == 1) {
            previous.setText(CC.translate("&8[&7«&8]"));
        } else {
            previous.setText(CC.translate("&8[&6«&8]"));
            previous.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(CC.translate("&f&l» &eCliquez-ici pour accéder à la page " + (page - 1)))
            }));
            previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command + " help " + (page - 1)));
        }

        TextComponent next = new TextComponent();
        if (page == getTotalPages()) {
            next.setText(CC.translate("&8[&7»&8]"));
        } else {
            next.setText(CC.translate("&8[&6»&8]"));
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(CC.translate("&f&l» &eCliquez-ici pour accéder à la page " + (page + 1)))
            }));
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command + " help " + (page + 1)));
        }

        player.spigot().sendMessage(textComponent, previous, next);
        player.sendMessage(getEmptyLine());
    }

    public static String getEmptyLine() {
        String start = Strings.repeat("-", 43 / 2);
        String end = Strings.repeat("-", 43 / 2);

        StringBuilder startBuilder = new StringBuilder("&7&m");
        for (int i = 0; i < start.length(); i++) {
            startBuilder.append(start.charAt(i));
            if (i == start.length() / 3) {
                startBuilder.append("&6&m");
            } else if (i == start.length() / 3 * 2) {
                startBuilder.append("&8&m");
            }
        }

        StringBuilder endBuilder = new StringBuilder("&8&m");
        for (int i = 0; i < end.length(); i++) {
            endBuilder.append(end.charAt(i));
            if (i == end.length() / 3) {
                endBuilder.append("&6&m");
            } else if (i == end.length() / 3 * 2) {
                endBuilder.append("&7&m");
            }
        }

        return CC.translate(startBuilder.toString() + endBuilder);
    }

    public static String getLineWithText(String text) {
        StringBuilder start = new StringBuilder("--");
        StringBuilder end = new StringBuilder("--");

        int textLength = text.length();

        for (int i = 0; i < 18 - (textLength / 2); i++) {
            start.append("-");
            end.insert(0, "-");
        }

        if (textLength % 2 == 0) {
            end.insert(0, "-");
        }

        StringBuilder startBuilder = new StringBuilder("&7&m");
        for (int i = 0; i < start.length(); i++) {
            startBuilder.append(start.charAt(i));
            if (i == start.length() / 3) {
                startBuilder.append("&6&m");
            } else if (i == start.length() / 3 * 2) {
                startBuilder.append("&8&m");
            }
        }

        StringBuilder endBuilder = new StringBuilder("&8&m");
        for (int i = 0; i < end.length(); i++) {
            endBuilder.append(end.charAt(i));
            if (i == end.length() / 3) {
                endBuilder.append("&6&m");
            } else if (i == end.length() / 3 * 2) {
                endBuilder.append("&7&m");
            }
        }

        return CC.translate(startBuilder + "&8 &e" + text + " &8" + endBuilder);
    }
}
