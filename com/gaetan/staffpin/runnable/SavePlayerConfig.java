/*
 * StaffPin: Staff security plugin.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
     * Saving the pin to a config
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
