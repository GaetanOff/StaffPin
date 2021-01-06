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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            final PlayerData playerData = new PlayerData(player, this.staffPlugin, this.configManager);
            this.staffPlugin.getPlayers().put(player, playerData);

            playerData.join();
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission(this.configManager.getPinPermission()) && this.staffPlugin.getPlayers().containsKey(player))
            this.staffPlugin.getPlayers().remove(player).leave();
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            final PlayerData playerData = this.staffPlugin.getPlayer(player);

            if (this.staffPlugin.getPlayers().containsKey(player) && !playerData.isLogin()) {
                event.setCancelled(true);

                if (event.getMessage().equals(playerData.getPin())) {
                    playerData.correct();
                    return;
                }

                TaskUtil.run((() -> player.kickPlayer(this.configManager.getIncorrectPin())));
            }
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            if (this.staffPlugin.getPlayers().containsKey(player) && !this.staffPlugin.getPlayer(player).isLogin()) {
                Message.tell(player, this.configManager.getEnterPin());
                event.setCancelled(true);
            }
        }
    }
}
