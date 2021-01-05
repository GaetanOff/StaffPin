package com.gaetan.staffpin.command;

import com.gaetan.api.command.utils.annotation.Command;
import com.gaetan.api.command.utils.command.Context;
import com.gaetan.api.command.utils.target.CommandTarget;
import com.gaetan.api.message.Message;
import com.gaetan.staffpin.StaffPlugin;
import com.gaetan.staffpin.data.PlayerData;
import com.gaetan.staffpin.enums.Lang;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class PinCommand {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Constructor for the PinCommand class.
     *
     * @param staffPlugin Reference to the main class
     */
    public PinCommand(final StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
    }

    /**
     * Command to show the usage message.
     *
     * @param context The command argument
     */
    @Command(name = "pin", permission = "pin.use", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        this.usage((Player) context.getSender());
    }

    /**
     * Command to set your pin.
     *
     * @param context The command argument
     */
    @Command(name = "pin.set", permission = "pin.use", target = CommandTarget.PLAYER)
    public void handleCommand_Set(final Context<ConsoleCommandSender> context, final String pin) {
        final Player player = (Player) context.getSender();
        final PlayerData playerData = this.staffPlugin.getPlayer(player);

        playerData.setPin(pin);
        Message.tell(player, Lang.PIN_SET.getText());
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
