package com.gaetan.staffpin.listener;

import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.config.ConfigManager;
import com.gaetan.staffpin.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class PlayerListener implements Listener {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Reference to the ConfigManager
     */
    private final ConfigManager configManager;

    /**
     * Constructor for the PlayerListener class.
     *
     * @param staffPlugin   Reference to te main class
     * @param configManager Reference to the ConfigManager class
     */
    public PlayerListener(final StaffPlugin staffPlugin, final ConfigManager configManager) {
        this.staffPlugin = staffPlugin;
        this.configManager = configManager;
        this.staffPlugin.getServer().getPluginManager().registerEvents(this, this.staffPlugin);
    }

    /**
     * When a player with the pin permission join the server
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            final PlayerData playerData = new PlayerData(player, this.staffPlugin, this.configManager);
            this.staffPlugin.getPlayers().put(player.getUniqueId(), playerData);

            playerData.join();
        }
    }

    /**
     * When a player with the pin permission left the server
     */
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();

        if (event.getPlayer().hasPermission(this.configManager.getPinPermission()) && this.staffPlugin.getPlayers().containsKey(uuid))
            this.staffPlugin.getPlayers().remove(uuid).leave();
    }

    /**
     * When a player with the pin permission talk in the chat
     * Note: This is listened async so I have to resync it
     */
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = event.getPlayer().getUniqueId();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            final PlayerData playerData = this.staffPlugin.getPlayer(uuid);

            if (this.staffPlugin.getPlayers().containsKey(uuid) && !playerData.isLogin()) {
                event.setCancelled(true);

                if (event.getMessage().equals(playerData.getPin())) {
                    TaskUtil.run(playerData::correct);
                    return;
                }

                TaskUtil.run((() -> player.kickPlayer(this.configManager.getIncorrectPin())));
            }
        }
    }

    /**
     * When a player with the pin permission do command in the chat
     */
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = event.getPlayer().getUniqueId();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            if (this.staffPlugin.getPlayers().containsKey(uuid) && !this.staffPlugin.getPlayer(uuid).isLogin()) {
                Message.tell(player, this.configManager.getEnterPin());
                event.setCancelled(true);
            }
        }
    }
}
