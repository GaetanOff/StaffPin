package com.gaetan.staffpin;

import com.gaetan.api.message.Message;
import com.gaetan.api.plugin.GCore;
import com.gaetan.staffpin.command.PinCommand;
import com.gaetan.staffpin.config.ConfigManager;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.listener.PlayerListener;
import com.gaetan.staffpin.runnable.MoveRunnable;
import com.google.common.collect.Maps;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

public final class StaffPlugin extends GCore {
    /**
     * Map to stock the PayerData
     */
    private final Map<Player, PlayerData> players = Maps.newConcurrentMap();

    /**
     * Reference to the ConfigManager
     */
    private ConfigManager configManager;

    /**
     * Method to launch the plugin
     * Note: This is the same as the classic onEnable
     */
    @Override
    protected void onPluginStart() {
        this.saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.registerCommands(new PinCommand(this, this.configManager));
    }

    /**
     * This is trigger when the server finished loading
     */
    @Override
    protected void onPluginLoad() {
        final FileConfiguration config = this.getConfig();

        this.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(config.getString("permission.pin")))
                .forEach(player -> player.kickPlayer(Message.tl(config.getString("lang.cant_reload"))));
    }

    /**
     * Method to register listener
     */
    @Override
    protected void registerListener() {
        new PlayerListener(this, this.configManager);
        new MoveRunnable(this, this.configManager);
    }

    /**
     * Getter to get the PlayerData of a specific player.
     *
     * @param player player
     * @return The PlayerData of the choosen player
     */
    public PlayerData getPlayer(final Player player) {
        return this.players.get(player);
    }

    /**
     * Getter to get the Map of all PlayerData.
     *
     * @return The PlayerData of the choosen player
     */
    public Map<Player, PlayerData> getPlayers() {
        return this.players;
    }
}
