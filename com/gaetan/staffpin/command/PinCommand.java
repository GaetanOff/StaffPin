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
    private final StaffPlugin staffPlugin;

    /**
     * Constructor for the PinCommand class.
     *
     * @param staffPlugin reference to the main class
     */
    public PinCommand(final StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
    }

    /**
     * Show the global usage message.
     *
     * @param context command argument
     */
    @Command(name = "pin", permission = "pin.use", target = CommandTarget.PLAYER)
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        this.usage((Player) context.getSender());
    }

    /**
     * Set your pin.
     *
     * @param context command argument
     */
    @Command(name = "pin.set", permission = "pin.use", target = CommandTarget.PLAYER)
    public void handleCommand_Set(final Context<ConsoleCommandSender> context, final String pin) {
        final Player player = (Player) context.getSender();
        final PlayerData playerData = this.staffPlugin.getPlayer(player);

        playerData.setPin(pin);
        Message.tell(player, Lang.PIN_SET.getText());
    }

    /**
     * Send the help message.
     *
     * @param player player to send the help message
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
