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

package com.gaetan.staffpin;

import com.gaetan.api.annotation.GaetanApplication;
import com.gaetan.api.message.Message;
import com.gaetan.api.plugin.SimplePlugin;
import com.gaetan.staffpin.command.PinCommand;
import com.gaetan.staffpin.config.ConfigManager;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.listener.PlayerListener;
import com.gaetan.staffpin.runnable.MoveRunnable;
import com.google.common.collect.Maps;
import org.bukkit.configuration.file.FileConfiguration;

import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@GaetanApplication(name = "StaffPin", authors = "GaetanOff", version = "1.1.3", main = "com.gaetan.staffpin.StaffPlugin", depend = "")
public final class StaffPlugin extends SimplePlugin {
    /**
     * Map to stock the PayerData
     */
    private final Map<UUID, PlayerData> players = Maps.newConcurrentMap();

    /**
     * Map to cache the ip
     */
    private final Map<UUID, InetAddress> cache = Maps.newConcurrentMap();

    /**
     * Reference to the ES Thread Pool
     */
    private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Reference to the ConfigManager
     */
    private ConfigManager configManager;

    /**
     * When the plugin start.
     * Note: This is the same as the classic onEnable
     */
    @Override
    protected void onPluginStart() {
        this.saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.registerCommands(new PinCommand(this, this.configManager));
    }

    /**
     * When the plugin load.
     * Note: This is the same as the classic onLoad
     */
    @Override
    protected void onPluginLoad() {
        final FileConfiguration config = this.getConfig();

        this.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(config.getString("permission.pin")))
                .forEach(player -> player.kickPlayer(Message.tl(config.getString("lang.cant_reload"))));
    }

    /**
     * Method to register listener.
     * Note: This will be trigger after the loading of the server
     */
    @Override
    protected void registerListener() {
        new PlayerListener(this, this.configManager);
        new MoveRunnable(this, this.configManager);
    }

    /**
     * Getter to get the PlayerData of a specific player with his UUID.
     * Note: The player must have the pin permission.
     *
     * @param uuid The choosen UUID
     * @return The PlayerData of the choosen UUID
     */
    public PlayerData getPlayer(final UUID uuid) {
        return this.players.get(uuid);
    }

    /**
     * Getter to get the Map of all PlayerData.
     *
     * @return The map containing all the UUID and PlayerData with the pin permission
     */
    public Map<UUID, PlayerData> getPlayers() {
        return this.players;
    }

    /**
     * Getter to get the reference to the ES Thread Pool.
     *
     * @return The reference to the ES Thread Pool.
     */
    public ExecutorService getThreadPool() {
        return this.threadPool;
    }

    /**
     * Getter to get the Map of all the cache.
     *
     * @return The map containing all the cache
     */
    public Map<UUID, InetAddress> getCache() {
        return this.cache;
    }
}
