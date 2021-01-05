package com.gaetan.staffpin;

import com.gaetan.api.plugin.GCore;
import com.gaetan.staffpin.command.PinCommand;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.listener.PlayerListener;
import com.gaetan.staffpin.runnable.MoveRunnable;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;

public final class StaffPlugin extends GCore {
    /**
     * Map to stock the PayerData
     */
    private final Map<Player, PlayerData> players = Maps.newConcurrentMap();

    /**
     * Method to launch the plugin
     * Note: This is the same as the classic onEnable
     */
    @Override
    protected void onPluginStart() {
        this.registerCommands(new PinCommand(this));
    }

    /**
     * This is trigger when the server finished loading
     */
    @Override
    protected void onPluginLoad() {
        this.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("pin.use"))
                .forEach(player -> player.kickPlayer("Merci de ne pas reload avec StafPin."));
    }

    /**
     * Method to register listener
     */
    @Override
    protected void registerListener() {
        new PlayerListener(this);
        new MoveRunnable(this);
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
