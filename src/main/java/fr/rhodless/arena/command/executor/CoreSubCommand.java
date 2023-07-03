package fr.rhodless.arena.command.executor;

import fr.rhodless.arena.command.CommandHandler;
import fr.rhodless.arena.command.annotations.MainCommand;
import fr.rhodless.arena.command.annotations.Param;
import fr.rhodless.arena.command.annotations.SubCommand;
import fr.rhodless.arena.command.help.HelpCommand;
import fr.rhodless.arena.command.parameter.PType;
import fr.rhodless.arena.utils.text.CC;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class CoreSubCommand extends org.bukkit.command.Command {
    private final HelpCommand helpCommand;
    private final CommandHandler coreCommandHandler;
    private final MainCommand command;
    private final Method[] methods;
    private final Object instance;


    public CoreSubCommand(CommandHandler coreCommandHandler, Object instance, MainCommand command, String usage, List<String> aliases, Method... methods) {
        super(command.names()[0], command.description(), usage, aliases);
        this.coreCommandHandler = coreCommandHandler;
        this.command = command;
        this.methods = methods;
        this.instance = instance;

        List<SubCommand> subCommands = new ArrayList<>();
        for (Method method : methods) {
            SubCommand subCommand = method.getAnnotation(SubCommand.class);
            if (subCommand == null) continue;

            String description = subCommand.description();

            if (!description.equalsIgnoreCase(""))
                subCommands.add(subCommand);
        }

        this.helpCommand = new HelpCommand(description, subCommands);
    }

    @Override
    @SneakyThrows
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 0 || strings[0] == null || strings[0].equals("")) {
            strings = new String[]{command.helpCommand()};
        }

        if (strings[0].equals(command.helpCommand())) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "This command can only be executed as a player.");
                return false;
            }

            int page = 1;
            if (strings.length > 1) {
                try {
                    page = Integer.parseInt(strings[1]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(CC.prefix("&cLa page doit être un nombre."));
                    return false;
                }
            }

            helpCommand.display((Player) commandSender, s, page);
            return true;
        }

        ArrayList<String> abc = new ArrayList<>(Arrays.asList(strings));
        String commandName = strings[0];
        abc.remove(0);
        String[] args = abc.toArray(new String[0]);
        Method method = null;
        for (Method m : methods) {
            if (Arrays.stream(m.getDeclaredAnnotation(SubCommand.class).names()).anyMatch(commandName::equalsIgnoreCase)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            if (commandSender instanceof Player)
                commandSender.sendMessage(CC.prefix("&cCette sous-commande n'existe pas."));
            return false;
        }
        SubCommand subCommand = method.getDeclaredAnnotation(SubCommand.class);

        List<Object> parameters = new ArrayList<>();

        if (method.getParameterTypes()[0].equals(ConsoleCommandSender.class)) {
            if (!(commandSender instanceof ConsoleCommandSender)) {
                commandSender.sendMessage(ChatColor.RED + "This command can only be executed with the console.");
                return false;
            }
        }

        if (method.getParameterTypes()[0].equals(Player.class)) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "This command can only be executed as a player.");
                return false;
            }

            parameters.add(commandSender);
        } else parameters.add(commandSender);

        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission(command.permission()) && !command.permission().equals("none")) {
                commandSender.sendMessage(CC.prefix(coreCommandHandler.getPermissionMessage()));
                return false;
            }

            if (!commandSender.hasPermission(subCommand.permission()) && !subCommand.permission().equals("none")) {
                commandSender.sendMessage(CC.prefix(coreCommandHandler.getPermissionMessage()));
                return false;
            }
        }


        if (method.getParameterTypes().length > 1) {
            for (int index = 1; index < method.getParameterTypes().length; index++) {
                Param param = null;

                for (Annotation annotation : method.getParameterAnnotations()[index]) {
                    if (annotation instanceof Param) {
                        param = (Param) annotation;
                        break;
                    }
                }

                if (param == null) {
                    commandSender.sendMessage(ChatColor.RED + "Parameter annotation is null ?!");
                    return false;
                }

                if (args.length == 0) {
                    boolean bool = false;
                    int i = 0;
                    for (Annotation[] annotationArray : method.getParameterAnnotations()) {
                        for (Annotation annotation : annotationArray) {
                            if (annotation instanceof Param) {
                                param = (Param) annotation;
                                if (param.baseValue().equalsIgnoreCase("")) bool = true;
                                else
                                    parameters.add(coreCommandHandler.getTypesMap().get(method.getParameterTypes()[i]).transform(commandSender, param.baseValue()));
                            }
                        }
                        i++;
                    }

                    if (bool) {
                        StringBuilder usage = new StringBuilder();
                        for (Annotation[] annotations : method.getParameterAnnotations()) {
                            for (Annotation annotation : annotations) {
                                if (annotation instanceof Param) {
                                    param = (Param) annotation;
                                    usage.append(!param.baseValue().equals("") ? '[' + param.name() + ']' : '<' + param.name() + '>').append(" ");
                                }
                            }
                        }

                        String name = subCommand.names()[0];
                        if (commandSender instanceof Player)
                            commandSender.sendMessage(CC.prefix("&cMerci d'utiliser /" + s + " " + name + " " + usage));
                        return false;
                    }
                } else {
                    if (args.length <= index - 1 || args[index - 1] == null || args[index - 1].equals("")) {
                        if (param.baseValue().equalsIgnoreCase("")) {
                            StringBuilder usage = new StringBuilder();
                            for (Annotation[] annotations : method.getParameterAnnotations()) {
                                for (Annotation annotation : annotations) {
                                    if (annotation instanceof Param) {
                                        param = (Param) annotation;
                                        usage.append(!param.baseValue().equals("") ? '[' + param.name() + ']' : '<' + param.name() + '>').append(" ");
                                    }
                                }
                            }

                            String name = (subCommand.names()[0]);
                            if (commandSender instanceof Player)
                                commandSender.sendMessage(CC.prefix("&cMerci d'utiliser /" + s + " " + name + " " + usage));
                            return false;
                        }
                        if (param.wildcard()) {
                            parameters.add(coreCommandHandler.getTypesMap().get(method.getParameterTypes()[index]).transform(commandSender, param.baseValue()));
                            break;
                        } else {
                            parameters.add(coreCommandHandler.getTypesMap().get(method.getParameterTypes()[index]).transform(commandSender, param.baseValue()));
                        }
                    } else if (param.wildcard()) {
                        StringBuilder sb = new StringBuilder();

                        for (int arg = index - 1; arg < args.length; arg++) {
                            sb.append(args[arg]).append(" ");
                        }

                        parameters.add(coreCommandHandler.getTypesMap().get(method.getParameterTypes()[index]).transform(commandSender, sb.toString()));
                        break;
                    } else
                        parameters.add(coreCommandHandler.getTypesMap().get(method.getParameterTypes()[index]).transform(commandSender, args[index - 1]));
                }
            }
        }

        for (Object parameter : parameters) {
            if (parameter == null) {
                return false;
            }
        }

        if (subCommand.async()) {
            Method finalMethod = method;
            ForkJoinPool.commonPool().execute(() -> {
                try {
                    finalMethod.invoke(instance, parameters.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                method.invoke(instance, parameters.toArray());
            } catch (IllegalArgumentException ignored) {
            }
        }
        return true;
    }

    @Override
    @SneakyThrows
    public List<String> tabComplete(CommandSender sender, String s, String[] strings) {
        if (!(sender instanceof Player)) return (null);

        Player player = (Player) sender;

        if (strings.length == 0 || strings[0] == null || strings[0].equals("")) {
            List<String> toReturn = new ArrayList<>();
            for (Method m : methods) {
                toReturn.add(m.getDeclaredAnnotation(SubCommand.class).names()[0]);
            }
            return toReturn;
        }

        Param param = null;


        ArrayList<String> abc = new ArrayList<>(Arrays.asList(strings));
        String commandName = strings[0];
        abc.remove(0);
        String[] args = abc.toArray(new String[0]);
        Method method = null;
        for (Method m : methods) {
            if (Arrays.stream(m.getDeclaredAnnotation(SubCommand.class).names()).anyMatch(commandName::equalsIgnoreCase)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            List<String> toReturn = new ArrayList<>();
            for (Method m : methods) {
                for (String str : m.getDeclaredAnnotation(SubCommand.class).names()) {
                    if (StringUtils.startsWithIgnoreCase(str, commandName)) {
                        toReturn.add(str);
                    }
                }
            }
            return (toReturn);
        }

        int index = args.length - 1;
        if (args.length == 0 || args.length > (method.getParameterCount() - 1)) {
            return (new ArrayList<>());
        }
        for (Annotation annotation : method.getParameters()[args.length].getAnnotations()) {
            if (annotation instanceof Param) {
                param = (Param) annotation;
                break;
            }
        }

        if (param == null) return (new ArrayList<>());
        if (!Arrays.equals(param.tabCompleteFlags(), new String[]{""}))
            return (Arrays.asList(param.tabCompleteFlags()));
        PType<?> pType = coreCommandHandler.getTypesMap().get(method.getParameterTypes()[args.length]);
        if (pType == null) return (new ArrayList<>());

        if (param.wildcard()) {

            StringBuilder sb = new StringBuilder();

            for (int arg = index; arg < args.length - 1; arg++) {
                sb.append(args[arg]).append(" ");
            }

            return (pType.complete(player, sb.toString()));

        } else {
            return (pType.complete(player, args[index]));
        }
    }
}
