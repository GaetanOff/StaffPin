package com.gaetan.staffpin.enums;

import com.gaetan.api.message.Message;

public enum Lang {
    PREFIX(Message.RED + Message.BOLD + "StaffPin " + Message.GRAY + "• "),
    ENTER_PING(PREFIX.getText() + Message.GREEN + "Veuillez entrer votre code pin dans le chat pour pouvoir acceder au serveur."),
    ENTER_SUCESS(PREFIX.getText() + Message.GREEN + "Code correct, vous pouvez maintenant jouer."),
    ENTER_FAILED(PREFIX.getText() + Message.RED + "Code incorrect."),
    NO_PIN(PREFIX.getText() + Message.RED + "Vous n'avez pas de pin, cela peut-être dangereux ! N'oubliez pas d'en mettre un avec /pin set"),
    PIN_SET(PREFIX.getText() + Message.GREEN + "Votre pin vient d'être mis à jour."),
    TIME_EXCED(PREFIX.getText() + Message.RED + "Vous avez pris trop de temps à rentrer votre code pin.");

    private final String text;

    /**
     * Getter to get the text.
     *
     * @return The choosen text
     */
    public String getText() {
        return this.text;
    }

    Lang(final String text) {
        this.text = text;
    }
}
