package me.jortvlaming.hardcore.data;

import me.jortvlaming.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {
    public final UUID uuid;
    private final String dataPath;

    public boolean isDead;
    public int deathCount;

    public boolean isPendingRevive;
    public boolean hasTimerActive;

    public Location pendingRespawnLocation = null;

    public PlayerData(Player player) {
        this(player.getUniqueId());
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        dataPath = "data." + uuid.toString() + ".";

        FileConfiguration data = Hardcore.instance.data;

        isDead = data.getBoolean(dataPath + "isDead", false);
        deathCount = data.getInt(dataPath + "deathCount", 0);

        isPendingRevive = data.getBoolean(dataPath + "pendingRevive", false);
        hasTimerActive = data.getBoolean(dataPath + "timerActive", true);

        if (data.contains(dataPath + "pendingRespawnLocation")) {
            String path = dataPath + "pendingRespawnLocation.";
            if (!data.contains(path + "x") || !data.contains(path + "y") || !data.contains(path + "z") || !data.contains(path + "world")) {
                return;
            }

            int x = data.getInt(path + "x");
            int y = data.getInt(path + "y");
            int z = data.getInt(path + "z");
            int yaw = data.getInt(path + "yaw", 0);
            int pitch = data.getInt(path + "pitch", 0);
            String world = data.getString(path + "world", "world");

            pendingRespawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }
    }

    public void saveData() {
        FileConfiguration data = Hardcore.instance.data;

        data.set(dataPath + "isDead", isDead);
        data.set(dataPath + "deathCount", deathCount);

        data.set(dataPath + "pendingRevive", isPendingRevive);
        data.set(dataPath + "timerActive", hasTimerActive);

        if (pendingRespawnLocation == null) {
            data.set(dataPath + "pendingRespawnLocation", null);
        } else {
            String rPath = dataPath + "pendingRespawnLocation.";

            data.set(rPath + "x", pendingRespawnLocation.getX());
            data.set(rPath + "y", pendingRespawnLocation.getY());
            data.set(rPath + "z", pendingRespawnLocation.getZ());
            data.set(rPath + "yaw", pendingRespawnLocation.getYaw());
            data.set(rPath + "pitch", pendingRespawnLocation.getPitch());
            data.set(rPath + "world", pendingRespawnLocation.getWorld().getName());
        }
    }
}
