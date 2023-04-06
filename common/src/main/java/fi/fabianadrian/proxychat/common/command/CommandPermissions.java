package fi.fabianadrian.proxychat.common.command;

import java.util.Locale;

public enum CommandPermissions {
    ANNOUNCEMENTS, BROADCAST, CHANNEL, CHANNEL_LIST, CHANNEL_MUTE, MESSAGE, MESSAGES, MESSAGES_SPY, MESSAGES_TOGGLE, MESSAGES_TOGGLE_OVERRIDE, PROXYCHAT;

    private final String permission = "proxychat.command." + this.name().toLowerCase(Locale.ENGLISH).replace("_", ".");

    public String permission() {
        return permission;
    }
}