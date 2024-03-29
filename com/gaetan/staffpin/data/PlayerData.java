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

package com.gaetan.staffpin.data;

import com.gaetan.api.PlayerUtil;
import com.gaetan.api.annotation.Asynchrone;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public final class PlayerData {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Reference to the ConfigManager
     */
    private final ConfigManager configManager;

    /**
     * Reference to the Player
     */
    private final Player player;

    /**
     * The pin of the Player
     */
    private String pin;

    /**
     * Status of the login Player
     */
    private boolean login;

    /**
     * Last inventory of the Player
     */
    private ItemStack[] inventory, armor;

    /**
     * Last location of the Player
     */
    private Location location;

    /**
     * Last gamemode of the Player
     */
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
     * Method to save the pin in a file.
     * Note: Prefer to use asynchronously
     */
    @Asynchrone
    private void save() {
        if (this.configManager.isAsyncSave())
            new SavePlayerConfig(this.staffPlugin, this).run();
        else
            TaskUtil.run(new SavePlayerConfig(this.staffPlugin, this));
    }

    /**
     * Method to load the pin from the config and cache-it.
     * Note: Prefer to use asynchronously
     */
    @Asynchrone
    public void load() {
        if (this.configManager.isAsyncLoad())
            new LoadPlayerConfig(this.staffPlugin, this, this.configManager).run();
        else
            TaskUtil.run(new LoadPlayerConfig(this.staffPlugin, this, this.configManager));
    }

    /**
     * Method when player join the server.
     */
    @Asynchrone
    public void join() {
        this.load();

        if (this.configManager.isCacheEnabled() && this.staffPlugin.getCache().containsKey(this.player.getUniqueId()) && this.staffPlugin.getCache().get(this.player.getUniqueId()).equals(this.player.getAddress().getAddress())) {
            this.staffPlugin.getThreadRunnablePool().schedule(() -> Message.tell(this.player, this.configManager.getCacheLogin()), 20, TimeUnit.MILLISECONDS);
            this.login = true;
            return;
        }

        this.inventory = this.player.getInventory().getContents();
        this.armor = this.player.getInventory().getArmorContents();
        this.gameMode = this.player.getGameMode();
        this.location = this.player.getLocation();
        this.pinCooldown();

        this.staffPlugin.getThreadRunnablePool().schedule(() -> {
            if (this.pin != null) {
                TaskUtil.run(() -> {
                    this.player.setGameMode(GameMode.SPECTATOR);
                    this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 255));
                    this.clearInventory();
                });
                return;
            }

            this.login = true;
        }, 20, TimeUnit.MILLISECONDS);
    }

    /**
     * Method when player enter the correct code in the chat.
     */
    @Asynchrone
    public void correct() {
        Message.tell(this.player, this.configManager.getCorrectPin());

        this.setLogin(true);

        TaskUtil.run(() -> {
            this.clearInventory();
            this.player.getInventory().setContents(this.getInventory());
            this.player.getInventory().setArmorContents(this.getArmor());
            this.player.updateInventory();
            this.player.teleport(this.getLocation());
            this.player.setGameMode(this.getGameMode());
            this.player.removePotionEffect(PotionEffectType.BLINDNESS);
            this.setInventory(null);
        });

        if (this.configManager.isCacheEnabled())
            this.staffPlugin.getCache().put(this.player.getUniqueId(), this.player.getAddress().getAddress());
    }

    /**
     * Method to launch the pin cooldown.
     * Note: The player have 20 seconds to enter the pin
     */
    private void pinCooldown() {
        this.staffPlugin.getThreadRunnablePool().schedule(() -> {
            if (!this.isLogin())
                TaskUtil.run(() -> this.player.kickPlayer(this.configManager.getTimeFinish()));
        }, 1, TimeUnit.MINUTES);
    }

    /**
     * Method when player quit the server.
     */
    @Asynchrone
    public void leave() {
        if (this.inventory != null) {
            TaskUtil.run(() -> {
                this.clearInventory();
                this.player.getInventory().setContents(this.inventory);
                this.player.getInventory().setArmorContents(this.armor);
                this.player.setGameMode(this.getGameMode());
            });
        }
    }

    /**
     * Method to clear player inventory.
     * Note: This method is from my framework
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
     * Setter to set if player is login.
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
