package com.gaetan.staffpin.listener;

import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.enums.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {
    private final StaffPlugin staffPlugin;

    /**
     * Constructor for the PlayerListener class.
     *
     * @param staffPlugin reference to te main class
     */
    public PlayerListener(final StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
        this.staffPlugin.getServer().getPluginManager().registerEvents(this, this.staffPlugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission("pin.use")) {
            final PlayerData playerData = new PlayerData(player, this.staffPlugin);
            this.staffPlugin.getPlayers().put(player, playerData);

            playerData.join();
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission("pin.use") && this.staffPlugin.getPlayers().containsKey(player))
            this.staffPlugin.getPlayers().remove(player).leave();
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission("pin.use")) {
            final PlayerData playerData = this.staffPlugin.getPlayer(player);

            if (this.staffPlugin.getPlayers().containsKey(player) && !playerData.isLogin()) {
                event.setCancelled(true);

                if (event.getMessage().equals(playerData.getPin())) {
                    playerData.correct();
                    return;
                }

                TaskUtil.run((() -> player.kickPlayer(Lang.ENTER_FAILED.getText())));
            }
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission("pin.use")) {
            if (this.staffPlugin.getPlayers().containsKey(player) && !this.staffPlugin.getPlayer(player).isLogin()) {
                Message.tell(player, Lang.ENTER_PING.getText());
                event.setCancelled(true);
            }
        }
    }
}
