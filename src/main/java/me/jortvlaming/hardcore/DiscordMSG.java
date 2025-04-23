package me.jortvlaming.hardcore;

import com.eduardomcb.discord.webhook.WebhookClient;
import com.eduardomcb.discord.webhook.WebhookManager;
import com.eduardomcb.discord.webhook.models.Message;
import org.bukkit.configuration.file.FileConfiguration;

public class DiscordMSG {
    private static final WebhookManager manager;

    private final static String avatar;
    private final static String webhookName;
    private final static String webhookURL;

    private static boolean available;

    static {
        FileConfiguration config = Hardcore.instance.getConfig();

        avatar = config.getString("discord_webhook.avatar", null);
        if (avatar == null) {
            Hardcore.instance.getLogger().severe("No webhook avatar set in config.yml! (discord_webhook.avatar)");
        }

        String tmp_webhookName;
        tmp_webhookName = config.getString("discord_webhook.name", null);
        if (tmp_webhookName == null) {
            Hardcore.instance.getLogger().severe("No webhook name set in config.yml! (discord_webhook.name) defaulting to 'Hardcore'");
            tmp_webhookName = "Hardcore";
        }
        webhookName = tmp_webhookName;

        webhookURL = config.getString("discord_webhook.url", null);
        if (webhookURL == null) {
            Hardcore.instance.getLogger().severe("No webhook url set in config.yml! (discord_webhook.url) webhook will not be functional");
            available = false;
        }

        if (available) {
            manager = new WebhookManager();

            manager.setListener(new WebhookClient.Callback() {
                @Override
                public void onSuccess(String s) {
                    Hardcore.instance.getLogger().info("Successfully sent webhook message: " + s);
                }

                @Override
                public void onFailure(int i, String s) {
                    Hardcore.instance.getLogger().warning("Failed to send webhook message: (" + i + ") " + s);
                }
            });
        } else {
            manager = null;
        }
    }

    public static void sendMessage(String content) {
        if (!available || manager == null) return;
        Message message = new Message();

        if (avatar != null)
            message.setAvatarUrl(avatar);
        message.setUsername(webhookName);
        message.setContent(content);

        manager.setMessage(message);
        manager.exec();
    }
}
