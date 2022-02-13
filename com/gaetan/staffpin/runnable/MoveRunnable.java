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

import com.gaetan.api.annotation.Asynchrone;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.config.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

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
    @Asynchrone
    public MoveRunnable(final StaffPlugin staffPlugin, final ConfigManager configManager) {
        this.staffPlugin = staffPlugin;
        this.configManager = configManager;

        if (configManager.isAsyncMove())
            this.staffPlugin.getThreadRunnablePool()
                    .scheduleAtFixedRate(this, 0, configManager.getTickMove() / 20, TimeUnit.SECONDS);
        else
            this.runTaskTimer(this.staffPlugin, 0L, configManager.getTickMove());
    }

    /**
     * Teleport player to the waiting pin location
     * Note: This will be executed asynchronously every 2 seconds
     */
    @Override
    public void run() {
        this.staffPlugin.getPlayers().values().forEach(playerData -> {
            if (!playerData.isLogin() && playerData.getPin() != null) {
                Message.tell(playerData.getPlayer(), this.configManager.getEnterPin());
                TaskUtil.run(() -> playerData.getPlayer().teleport(playerData.getLocation()));
            }
        });
    }
}
