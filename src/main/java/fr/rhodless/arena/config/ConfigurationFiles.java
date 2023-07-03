package fr.rhodless.arena.config;

import fr.rhodless.arena.Arena;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
@Getter
@RequiredArgsConstructor
public enum ConfigurationFiles {
    CONFIG("config.yml"),

    ;

    private final String fileName;
    private final File dataFolder;

    ConfigurationFiles(String fileName) {
        this.fileName = fileName;
        this.dataFolder = Arena.getInstance().getDataFolder();
    }

    public void create(Logger logger) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("The file can not be empty...");
        }

        InputStream in = Arena.getInstance().getResource(fileName);
        if (in == null) {
            throw new IllegalArgumentException("The file '" + fileName + "' has not been found in the jar plugin");
        }

        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            logger.severe("Failed to create the file...");
        }

        File outFile = getFile();

        try {
            if (!outFile.exists()) {
                logger.info("The file '" + fileName + "' has not been found, creating it.");

                OutputStream out = Files.newOutputStream(outFile.toPath());
                byte[] buf = new byte[1024];
                int n;

                while ((n = in.read(buf)) >= 0) {
                    out.write(buf, 0, n);
                }

                out.close();
                in.close();

                if (!outFile.exists()) {
                    logger.severe("Failed to copy the file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return new File(dataFolder, fileName);
    }

    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }

    public void save(FileConfiguration config) {
        try {
            config.save(getFile());
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("An error occurred while trying to save file " + fileName + "...");
            e.printStackTrace();
        }
    }
}
