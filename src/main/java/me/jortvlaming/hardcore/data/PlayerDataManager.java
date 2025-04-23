package me.jortvlaming.hardcore.data;

import me.jortvlaming.hardcore.Hardcore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private static final HashMap<String, UUID> nameCache;

    static  {
        ConfigurationSection nameCacheSection = Hardcore.instance.data.getConfigurationSection("nameCache");

        if (nameCacheSection == null) {
            nameCacheSection = Hardcore.instance.data.createSection("nameCache");
        }

        nameCache = new HashMap<>();
        for (String s : nameCacheSection.getKeys(false)) {
            nameCache.put(s, UUID.fromString(nameCacheSection.getString(s, "")));
        }
    }

    public static void playerJoin(Player player) {
        loadPlayerData(player.getUniqueId());
        nameCache.put(player.getName().toLowerCase(), player.getUniqueId());
    }

    public static void playerLeave(Player player) {
        playerData.get(player.getUniqueId()).saveData();
        playerData.remove(player.getUniqueId());
        nameCache.put(player.getName(), player.getUniqueId());
    }

    public static void shutdown() {
        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            entry.getValue().saveData();
        }
    }

    @Nullable
    public static UUID nameToString(String s) {
        return nameCache.getOrDefault(s.toLowerCase(), null);
    }

    @Nullable
    public static PlayerData getPlayerData(UUID uuid) {
        return playerData.getOrDefault(uuid, loadPlayerData(uuid));
    }

    @Nullable
    public static PlayerData getPlayerData(String s) {
        UUID uuid = nameToString(s);
        if (uuid == null)
            return null;
        return getPlayerData(uuid);
    }

    private static PlayerData loadPlayerData(UUID uuid) {
        PlayerData data = new PlayerData(uuid);
        playerData.put(uuid, data);
        return data;
    }
}
