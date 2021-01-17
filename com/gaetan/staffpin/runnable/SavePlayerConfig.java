package com.gaetan.staffpin.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.data.PlayerData;

public final class SavePlayerConfig implements Runnable {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Reference to the PlayerData
     */
    private final PlayerData playerData;

    /**
     * Constructor for the SavePlayerConfig runnable.
     *
     * @param staffPlugin Reference to the main class
     * @param playerData  Reference to the PlayerData
     */
    public SavePlayerConfig(final StaffPlugin staffPlugin, final PlayerData playerData) {
        this.staffPlugin = staffPlugin;
        this.playerData = playerData;
    }

    /**
     * Saving the playerdata to a config
     * Note: This must be executed in async
     */
    @Override
    public void run() {
        if (this.playerData.getPin() != null) {
            final ConfigUtil config = new ConfigUtil(this.staffPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            config.getConfig().set("pin.string", this.playerData.getPin());
            config.save();
        }
    }
}
