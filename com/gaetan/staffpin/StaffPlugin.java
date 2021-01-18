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

import java.util.Map;
import java.util.UUID;

public final class StaffPlugin extends GCore {
    /**
     * Map to stock the PayerData
     */
    private final Map<UUID, PlayerData> players = Maps.newConcurrentMap();

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
     * Method to register listener
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
}
