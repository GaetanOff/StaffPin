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
     *
     * @param context The command argument
     */
    @Command(name = "pin", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();

        if (player.hasPermission(this.configManager.getPinPermission()))
            this.usage(player);
    }

    /**
     * Command to set your pin.
     *
     * @param context The command argument
     */
    @Command(name = "pin.set", target = CommandTarget.PLAYER)
    public void handleCommand_Set(final Context<ConsoleCommandSender> context, final String pin) {
        final Player player = (Player) context.getSender();

        if (player.hasPermission(this.configManager.getPinPermission())) {
            this.staffPlugin.getPlayer(player).setPin(pin);
            Message.tell(player, this.configManager.getPinSet());
        }
    }

    /**
     * Method to send the help message.
     *
     * @param player The player to send the message
     */
    private void usage(final Player player) {
        Message.tell(player, new String[]{
                "",
                Message.GOLD + Message.BOLD + "StaffPin" + Message.GRAY + Message.ITALIC + " (Gaetan#7171)",
                "",
                Message.YELLOW + "/pin" + Message.GRAY + " - Voir les commandes du plugin.",
                Message.YELLOW + "/pin set" + Message.GRAY + " - Changer votre code pin.",
                ""
        });
    }
}
