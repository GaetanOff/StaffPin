package com.gaetan.staffpin.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.enums.Lang;

import java.io.File;

public final class LoadPlayerConfig implements Runnable {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Reference to the PlayerData
     */
    private final PlayerData playerData;

    /**
     * Constructor for the LoadPlayerConfig runnable.
     *
     * @param staffPlugin Reference to the main class
     * @param playerData  Reference to the PlayerData
     */
    public LoadPlayerConfig(final StaffPlugin staffPlugin, final PlayerData playerData) {
        this.staffPlugin = staffPlugin;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        if (new File(this.staffPlugin.getDataFolder() + "/players", this.playerData.getPlayer().getUniqueId().toString() + ".yml").exists()) {
            final ConfigUtil config = new ConfigUtil(this.staffPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            this.playerData.setPin(config.getConfig().get("pin.string").toString());

            TaskUtil.run(() -> Message.tell(this.playerData.getPlayer(), Lang.ENTER_PING.getText()));
        } else {
            this.playerData.setLogin(true);
            TaskUtil.run(() -> Message.tell(this.playerData.getPlayer(), Lang.NO_PIN.getText()));
        }
    }
}
