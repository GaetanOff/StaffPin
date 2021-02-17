package com.gaetan.staffpin.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.config.ConfigManager;
import com.gaetan.staffpin.data.PlayerData;

import java.io.File;

public final class LoadPlayerConfig implements Runnable {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Reference to the ConfigManager
     */
    private final ConfigManager configManager;

    /**
     * Reference to the PlayerData
     */
    private final PlayerData playerData;

    /**
     * Constructor for the LoadPlayerConfig runnable.
     *
     * @param staffPlugin   Reference to the main class
     * @param playerData    Reference to the PlayerData
     * @param configManager Reference to the ConfigManager class
     */
    public LoadPlayerConfig(final StaffPlugin staffPlugin, final PlayerData playerData, final ConfigManager configManager) {
        this.staffPlugin = staffPlugin;
        this.playerData = playerData;
        this.configManager = configManager;
    }

    /**
     * Loading the pin from a config
     * Note: This must be executed in async
     */
    @Override
    public void run() {

        if (new File(this.staffPlugin.getDataFolder() + "/players", this.playerData.getPlayer().getUniqueId().toString() + ".yml").exists()) {
            this.playerData.setPin(new ConfigUtil(this.staffPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString())
                    .getConfig().get("pin.string").toString());

            TaskUtil.run(() -> Message.tell(this.playerData.getPlayer(), this.configManager.getEnterPin()));
        } else {
            this.playerData.setLogin(true);
            TaskUtil.run(() -> Message.tell(this.playerData.getPlayer(), this.configManager.getNoPin()));
        }
    }
}
