package com.gaetan.staffpin.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.config.ConfigManager;
import com.gaetan.staffpin.data.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

public final class MoveRunnable extends BukkitRunnable {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;


    /**
     * Reference to the ConfigManager
     */
    private final ConfigManager configManager;

    /**
     * Constructor for the MoveRunnable runnable.
     *
     * @param staffPlugin   Reference to the main class
     * @param configManager Reference to the ConfigManager class
     */
    public MoveRunnable(final StaffPlugin staffPlugin, final ConfigManager configManager) {
        this.staffPlugin = staffPlugin;
        this.configManager = configManager;
        this.runTaskTimer(this.staffPlugin, 0L, 40L);
    }

    /**
     * Teleport player to the waiting pin location
     * Note: This will be executed every 2 seconds
     */
    @Override
    public void run() {
        this.staffPlugin.getPlayers().keySet().forEach(player -> {
            final PlayerData playerData = this.staffPlugin.getPlayer(player);
            if (!playerData.isLogin() && playerData.getPin() != null) {
                Message.tell(player, this.configManager.getEnterPin());
                player.teleport(playerData.getLocation());
            }
        });
    }
}
