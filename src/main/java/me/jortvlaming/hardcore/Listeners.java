package me.jortvlaming.hardcore;

import com.crystalcoding.api.StringUtils;
import me.jortvlaming.hardcore.data.PlayerData;
import me.jortvlaming.hardcore.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Listeners implements Listener {
    public Listeners(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerDataManager.playerJoin(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        PlayerDataManager.playerLeave(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        String deathMSG = event.getDeathMessage();
        event.setDeathMessage(null);

        Bukkit.broadcastMessage(StringUtils.color("&c&l☠ &r" + dead.getDisplayName() + "&c has died!"));
        Bukkit.broadcastMessage(StringUtils.color("&7   " + deathMSG));

        PlayerData pd = PlayerDataManager.getPlayerData(dead);

        // TODO add support for grace periods

        dead.setGameMode(GameMode.SPECTATOR);

        pd.isDead = true;
        pd.deathCount += 1;
        pd.pendingRespawnLocation = dead.getLocation();

        pd.saveData();

        dead.getWorld().spawnEntity(dead.getLocation(), EntityType.LIGHTNING_BOLT);

        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();

        PlayerData pd = PlayerDataManager.getPlayerData(p);

        if (pd.pendingRespawnLocation != null) {
            event.setRespawnLocation(pd.pendingRespawnLocation);

            pd.pendingRespawnLocation = null;
            pd.saveData();
        }
    }
}
