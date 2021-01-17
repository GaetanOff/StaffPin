package com.gaetan.staffpin.data;

import com.gaetan.api.PlayerUtil;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.config.ConfigManager;
import com.gaetan.staffpin.runnable.LoadPlayerConfig;
import com.gaetan.staffpin.runnable.SavePlayerConfig;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerData {
    private final StaffPlugin staffPlugin;
    private final ConfigManager configManager;
    private final Player player;
    private String pin;
    private boolean login;
    private ItemStack[] inventory, armor;
    private Location location;
    private GameMode gameMode;

    /**
     * Constructor for the PlayerData class.
     *
     * @param player        The data of this player
     * @param staffPlugin   Reference to the main class
     * @param configManager Reference to the ConfigManager class
     */
    public PlayerData(final Player player, final StaffPlugin staffPlugin, final ConfigManager configManager) {
        this.staffPlugin = staffPlugin;
        this.configManager = configManager;
        this.player = player;
        this.pin = null;
    }

    /**
     * Method to save the pin in a config
     */
    private void save() {
        this.staffPlugin.getServer().getScheduler().runTaskAsynchronously(this.staffPlugin, new SavePlayerConfig(this.staffPlugin, this));
    }

    /**
     * Method to load the pin from the config and cache-it
     */
    public void load() {
        this.staffPlugin.getServer().getScheduler().runTaskAsynchronously(this.staffPlugin, new LoadPlayerConfig(this.staffPlugin, this, this.configManager));
    }

    /**
     * Method when player join the server
     */
    public void join() {
        this.load();

        TaskUtil.runLater(() -> {
            if (this.pin != null) {
                this.inventory = this.player.getInventory().getContents();
                this.armor = this.player.getInventory().getArmorContents();
                this.gameMode = this.player.getGameMode();
                this.location = this.player.getLocation();
                this.player.setGameMode(GameMode.SPECTATOR);
                this.clearInventory();
                this.pinCooldown();
                return;
            }

            this.login = true;
        }, 2L);
    }

    /**
     * Method when player enter the correct code in the chat
     */
    public void correct() {
        Message.tell(this.player, this.configManager.getCorrectPin());

        this.setLogin(true);
        this.clearInventory();
        this.player.getInventory().setContents(this.getInventory());
        this.player.getInventory().setArmorContents(this.getArmor());
        this.player.updateInventory();
        this.player.teleport(this.getLocation());
        this.player.setGameMode(this.getGameMode());
        this.setInventory(null);
    }

    /**
     * Method to launch the pin cooldown
     */
    private void pinCooldown() {
        TaskUtil.runLater(() -> {
            if (!this.isLogin())
                this.player.kickPlayer(this.configManager.getTimeFinish());

        }, 400L);
    }

    /**
     * Method when player left the server
     */
    public void leave() {
        if (this.inventory != null) {
            this.clearInventory();
            this.player.getInventory().setContents(this.inventory);
            this.player.getInventory().setArmorContents(this.armor);
            this.player.setGameMode(this.getGameMode());
        }
    }

    /**
     * Method to clear player inventory
     */
    public void clearInventory() {
        PlayerUtil.clearInventory(this.player);
    }

    /**
     * Setter to set the pin and save it.
     *
     * @param pin The new pin
     */
    public void setPin(final String pin) {
        this.pin = pin;
        this.save();
    }

    /**
     * Get the reference for the Player of this data.
     *
     * @return The reference for the Player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Getter to get the pin.
     *
     * @return The player pin
     */
    public String getPin() {
        return this.pin;
    }

    /**
     * Setter to set if player is login or not.
     *
     * @param login The state of his login
     */
    public void setLogin(final boolean login) {
        this.login = login;
    }

    /**
     * Getter to get if the player is login.
     *
     * @return If the player is login
     */
    public boolean isLogin() {
        return this.login;
    }

    /**
     * Setter to set the last player inventory.
     *
     * @param inventory The last player inventory
     */
    public void setInventory(final ItemStack[] inventory) {
        this.inventory = inventory;
    }

    /**
     * Getter to get the last player inventory.
     *
     * @return The last player inventory
     */
    public ItemStack[] getInventory() {
        return this.inventory;
    }

    /**
     * Getter to get the last player armor.
     *
     * @return The last player armor
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * Getter to get the last player location.
     *
     * @return The last player location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Getter to get the last player gamemode.
     *
     * @return The last player gamemode
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }
}
