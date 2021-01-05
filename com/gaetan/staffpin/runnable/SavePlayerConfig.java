package com.gaetan.staffpin.runnable;

import com.gaetan.api.ConfigUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.data.PlayerData;

public final class SavePlayerConfig implements Runnable {
    private final StaffPlugin staffPlugin;
    private final PlayerData playerData;

    /**
     * Constructor for the SavePlayerConfig runnable.
     *
     * @param staffPlugin refeference to the main class
     * @param playerData  the reference of the data
     */
    public SavePlayerConfig(final StaffPlugin staffPlugin, final PlayerData playerData) {
        this.staffPlugin = staffPlugin;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        if (this.playerData.getPin() != null) {
            final ConfigUtil config = new ConfigUtil(this.staffPlugin, "/players", this.playerData.getPlayer().getUniqueId().toString());
            config.getConfig().set("pin.string", this.playerData.getPin());
            config.save();
        }
    }
}
