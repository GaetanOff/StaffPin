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

package com.gaetan.staffpin.command;

import com.gaetan.api.command.utils.annotation.Command;
import com.gaetan.api.command.utils.command.Context;
import com.gaetan.api.command.utils.target.CommandTarget;
import com.gaetan.api.message.Message;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.config.ConfigManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class PinCommand {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Reference to the ConfigManager
     */
    private final ConfigManager configManager;

    /**
     * Constructor for the PinCommand class.
     *
     * @param staffPlugin   Reference to the main class
     * @param configManager Reference to the ConfigManager class
     */
    public PinCommand(final StaffPlugin staffPlugin, final ConfigManager configManager) {
        this.staffPlugin = staffPlugin;
        this.configManager = configManager;
    }

    /**
     * Command to show the usage message.
     * Note: This can only be used by a player with the pin permission
     *
     * @param context The command argument
     */
    @Command(name = "pin", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        this.staffPlugin.getThreadPool().execute(() -> {
            final Player player = (Player) context.getSender();

            if (player.hasPermission(this.configManager.getPinPermission()))
                Message.tell(player, new String[]{
                        "",
                        Message.GOLD + Message.BOLD + "StaffPin" + Message.GRAY + Message.ITALIC + " (Gaetan#0099)",
                        "",
                        Message.YELLOW + "/pin" + Message.GRAY + " - See the plugins commands.",
                        Message.YELLOW + "/pin set" + Message.GRAY + " - Change your pin.",
                        ""
                });
        });
    }

    /**
     * Command to set a pin.
     * Note: This can only be used by a player with the pin permission
     *
     * @param context The command argument
     */
    @Command(name = "pin.set", target = CommandTarget.PLAYER)
    public void handleCommand_Set(final Context<ConsoleCommandSender> context) {
        this.staffPlugin.getThreadPool().execute(() -> {
            final Player player = (Player) context.getSender();

            if (player.hasPermission(this.configManager.getPinPermission())) {
                if (context.getArgs().length == 0) {
                    Message.tell(player, this.configManager.getPinUsage());
                    return;
                }

                this.staffPlugin.getPlayer(player.getUniqueId()).setPin(context.getArgs()[0]);
                Message.tell(player, this.configManager.getPinSet());
            }
        });
    }
}
