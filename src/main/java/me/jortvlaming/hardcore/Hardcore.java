package me.jortvlaming.hardcore;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Hardcore extends JavaPlugin {
    static Hardcore instance;

    public File dataFile;
    public YamlConfiguration data;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic

        if (!getDataFolder().exists()) {
            boolean s = getDataFolder().mkdirs();

            if (s) {
                getLogger().info("Created plugin folder!");
            }
        }

        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                boolean s = dataFile.createNewFile();

                if (s) {
                    getLogger().info("Created data.yml!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        data = YamlConfiguration.loadConfiguration(dataFile);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        saveConfig();
    }
}
