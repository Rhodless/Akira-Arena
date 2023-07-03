package fr.rhodless.arena.command;

import fr.rhodless.arena.command.annotations.Command;
import fr.rhodless.arena.command.annotations.MainCommand;
import fr.rhodless.arena.command.annotations.Param;
import fr.rhodless.arena.command.annotations.SubCommand;
import fr.rhodless.arena.command.executor.CoreCommand;
import fr.rhodless.arena.command.executor.CoreSubCommand;
import fr.rhodless.arena.command.parameter.PData;
import fr.rhodless.arena.command.parameter.PType;
import fr.rhodless.arena.command.parameter.defaults.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
@Getter
public class CommandHandler {
    private final JavaPlugin plugin;
    private final Map<Class<?>, PType<?>> typesMap;

    @Setter
    private String permissionMessage = "&cVous n'avez pas la permission d'exécuter cette commande.";

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.typesMap = new HashMap<>();
        this.registerDefaultTypes();
    }

    public void registerCommands(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class)) continue;
            registerMethod(method, object);
        }
    }

    public void registerCommandWithSubCommands(Object object) {
        if (object.getClass().getAnnotation(MainCommand.class) == null) return;
        MainCommand mainCommandAnnotation = object.getClass().getAnnotation(MainCommand.class);

        List<Method> methodList = new ArrayList<>();

        List<Method> methods = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredMethods()));
        for (Method method : methods) {
            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            SubCommand commandAnnotation = method.getAnnotation(SubCommand.class);

            List<PData> parameterData = new ArrayList<>();

            for (int parameterIndex = 1; parameterIndex < method.getParameterTypes().length; parameterIndex++) {
                Param paramAnnotation = null;

                for (Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
                    if (annotation instanceof Param) {
                        paramAnnotation = (Param) annotation;
                        break;
                    }
                }

                if (paramAnnotation != null) {
                    Class<?> paramClass = method.getParameterTypes()[parameterIndex];
                    if (!this.typesMap.containsKey(paramClass)) {
                        plugin.getLogger().severe("[CoreCommandHandler] Class '" + paramClass.getSimpleName() + ".class' does not have an assigned type adapter (did you register it?)");
                        return;
                    }
                    parameterData.add(new PData(paramAnnotation, paramClass));
                } else {
                    plugin.getLogger().warning("[CoreCommandHandler] Method '" + method.getName() + "' has a parameter without a @Param annotation.");
                    return;
                }
            }

            StringBuilder usage = new StringBuilder("/").append(mainCommandAnnotation.names()[0]).append(" ").append(commandAnnotation.names()[0]);
            for (PData param : parameterData) {
                usage.append(" ").append(param.isRequired() ? "<" : "[").append(param.getName()).append(param.isRequired() ? ">" : "]");
            }

            methodList.add(method);
        }

        String mainUsage = "/" + mainCommandAnnotation.names()[0] + " " + mainCommandAnnotation.helpCommand();
        if (!mainCommandAnnotation.usage().equalsIgnoreCase("none")) mainUsage = mainCommandAnnotation.usage();

        List<String> aliases = new ArrayList<>();

        for (String alias : mainCommandAnnotation.names()) {
            if (alias.equalsIgnoreCase(mainCommandAnnotation.names()[0])) continue;
            aliases.add(alias);
        }

        CoreSubCommand command = new CoreSubCommand(this, object, mainCommandAnnotation, mainUsage, aliases, methodList.toArray(new Method[0]));
        getCommandMap().register(plugin.getName(), command);
    }

    /**
     * Register a Type adapter.
     *
     * @param from the PType object to register from. (IE: new WorldType())
     * @param to   the class to return when transformed. (IE: World.class)
     */
    public void registerType(PType<?> from, Class<?> to) {
        this.typesMap.put(to, from);
    }

    /**
     * Registers a method as a command.
     *
     * @param method   The method to register
     * @param instance The instance of the class to register the method to
     */
    private void registerMethod(Method method, Object instance) {

        Command commandAnnotation = method.getAnnotation(Command.class);
        List<PData> parameterData = new ArrayList<>();

        for (int parameterIndex = 1; parameterIndex < method.getParameterTypes().length; parameterIndex++) {
            Param paramAnnotation = null;

            for (Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
                if (annotation instanceof Param) {
                    paramAnnotation = (Param) annotation;
                    break;
                }
            }

            if (paramAnnotation != null) {
                Class<?> paramClass = method.getParameterTypes()[parameterIndex];
                if (!this.typesMap.containsKey(paramClass)) {
                    plugin.getLogger().severe("[CoreCommandHandler] Class '" + paramClass.getSimpleName() + ".class' does not have an assigned type adapter (did you register it?)");
                    return;
                }
                parameterData.add(new PData(paramAnnotation, paramClass));
            } else {
                plugin.getLogger().warning("[CoreCommandHandler] Method '" + method.getName() + "' has a parameter without a @Param annotation.");
                return;
            }
        }

        String name = commandAnnotation.names()[0];
        List<String> aliases = new ArrayList<>();
        for (String alias : commandAnnotation.names()) {
            if (alias.equalsIgnoreCase(name)) continue;
            aliases.add(alias);
        }
        StringBuilder usage = new StringBuilder("/").append(name);
        for (PData param : parameterData) {
            usage.append(" ").append(param.isRequired() ? "<" : "[").append(param.getName()).append(param.isRequired() ? ">" : "]");
        }

        CoreCommand command = new CoreCommand(this, method, instance, commandAnnotation, ChatColor.RED + usage.toString(), aliases);
        getCommandMap().register(plugin.getName(), command);
    }

    /**
     * Get the CommandMap for the server.
     *
     * @return The CommandMap for the server.
     */
    private SimpleCommandMap getCommandMap() {
        try {
            SimplePluginManager pluginManager = (SimplePluginManager) Bukkit.getPluginManager();

            Field field = pluginManager.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            return (SimpleCommandMap) field.get(pluginManager);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Registers the default parameter types.
     */
    private void registerDefaultTypes() {
        registerType(new DoubleType(), double.class);
        registerType(new FloatType(), float.class);
        registerType(new IntegerType(), int.class);
        registerType(new StringType(), String.class);
        registerType(new PlayerType(), Player.class);
        registerType(new WorldType(), World.class);
    }
}
