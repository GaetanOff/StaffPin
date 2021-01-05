package com.gaetan.staffpin.runnable;

import com.gaetan.api.message.Message;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.enums.Lang;
import org.bukkit.scheduler.BukkitRunnable;

public final class MoveRunnable extends BukkitRunnable {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Constructor for the MoveRunnable runnable.
     *
     * @param staffPlugin Reference to the main class
     */
    public MoveRunnable(final StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
        this.runTaskTimer(this.staffPlugin, 0L, 40L);
    }

    @Override
    public void run() {
        this.staffPlugin.getPlayers().keySet().forEach(player -> {
            final PlayerData playerData = this.staffPlugin.getPlayer(player);
            if (!playerData.isLogin() && playerData.getPin() != null) {
                Message.tell(player, Lang.ENTER_PING.getText());

                player.teleport(playerData.getLocation());
            }
        });
    }
}
