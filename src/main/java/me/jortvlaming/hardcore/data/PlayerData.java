package me.jortvlaming.hardcore.data;

import me.jortvlaming.hardcore.Hardcore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {
    public final UUID uuid;

    public boolean isDead;
    public boolean isPendingRevive;
    public boolean hasTimerActive;

    public PlayerData(Player player) {
        this(player.getUniqueId());
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;

        FileConfiguration data = Hardcore.instance.data;

        isDead = data.getBoolean("data." + uuid.toString() + ".isDead", false);
        isPendingRevive = data.getBoolean("data." + uuid.toString() + ".pendingRevive", false);
        hasTimerActive = data.getBoolean("data." + uuid.toString() + ".timerActive", true);
    }

    public void saveData() {
        FileConfiguration data = Hardcore.instance.data;

        data.set("data." + uuid.toString() + ".isDead", isDead);
        data.set("data." + uuid.toString() + ".pendingRevive", isPendingRevive);
        data.set("data." + uuid.toString() + ".timerActive", hasTimerActive);
    }
}
