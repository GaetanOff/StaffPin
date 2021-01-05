package com.gaetan.staffpin.data;

import com.gaetan.api.PlayerUtil;
import com.gaetan.api.message.Message;
import com.gaetan.api.runnable.TaskUtil;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.enums.Lang;
import com.gaetan.staffpin.runnable.LoadPlayerConfig;
import com.gaetan.staffpin.runnable.SavePlayerConfig;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerData {
    private final StaffPlugin staffPlugin;
    private final Player player;
    private String pin;
    private boolean login;
    private ItemStack[] inventory, armor;
    private Location location;
    private GameMode gameMode;

    /**
     * Constructor for the PlayerData class.
     *
     * @param player      the object of this player
     * @param staffPlugin refeference to the main class
     */
    public PlayerData(final Player player, final StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
        this.player = player;
        this.pin = null;
    }

    /**
     * Save the pin in a config.
     */
    private void save() {
        this.staffPlugin.getServer().getScheduler().runTaskAsynchronously(this.staffPlugin, new SavePlayerConfig(this.staffPlugin, this));
    }

    /**
     * Load the pin from the config.
     */
    public void load() {
        this.staffPlugin.getServer().getScheduler().runTaskAsynchronously(this.staffPlugin, new LoadPlayerConfig(this.staffPlugin, this));
    }

    /**
     * When player join the server.
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
     * When player enter the correct code in the chat.
     */
    public void correct() {
        Message.tell(this.player, Lang.ENTER_SUCESS.getText());

        this.setLogin(true);
        this.clearInventory();
        this.player.getInventory().setContents(this.getInventory());
        this.player.getInventory().setArmorContents(this.getArmor());
        this.player.updateInventory();
        this.player.teleport(this.getLocation());
        TaskUtil.run((() -> this.player.setGameMode(this.getGameMode())));
        this.setInventory(null);
    }

    /**
     * Launch the pin cooldown.
     */
    private void pinCooldown() {
        TaskUtil.runLater(() -> {
            if (!this.isLogin())
                this.player.kickPlayer(Lang.TIME_EXCED.getText());

        }, 400L);
    }

    /**
     * When player left the server.
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
     * Clear player inventory.
     */
    public void clearInventory() {
        PlayerUtil.clearInventory(this.player);
    }

    /**
     * Set the pin and save it.
     *
     * @param pin the pin of the player
     */
    public void setPin(final String pin) {
        this.pin = pin;
        this.save();
    }

    /**
     * Get the reference for the Player of this data.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the pin.
     */
    public String getPin() {
        return this.pin;
    }

    /**
     * Set the player login.
     *
     * @param login state of his login
     */
    public void setLogin(final boolean login) {
        this.login = login;
    }

    /**
     * Get if the player is login.
     */
    public boolean isLogin() {
        return this.login;
    }

    /**
     * Set the player last inventory.
     *
     * @param inventory last inventory
     */
    public void setInventory(final ItemStack[] inventory) {
        this.inventory = inventory;
    }

    /**
     * Get the player last inventory.
     */
    public ItemStack[] getInventory() {
        return this.inventory;
    }

    /**
     * Get the player last armor.
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * Get the player last location.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Get the player last gamemode.
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }
}
