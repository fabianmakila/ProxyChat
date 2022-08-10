package fi.fabianadrian.proxychat.command;

import java.util.Locale;

public enum CommandPermissions {
    ANNOUNCEMENTS, BROADCAST, CHANNEL, CHANNEL_LIST, CHANNEL_MUTE, MESSAGE, MESSAGES, MESSAGES_SPY, MESSAGES_TOGGLE, PROXYCHAT;

    private final String permission = "proxychat.command." + this.name().toLowerCase(Locale.ENGLISH).replace("_", ".");

    public String permission() {
        return permission;
    }
}
