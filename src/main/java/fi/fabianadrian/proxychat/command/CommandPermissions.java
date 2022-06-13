package fi.fabianadrian.proxychat.command;

//TODO Should this be enum?
public final class CommandPermissions {
    private static final String BASE = "proxychat.command.";

    public static final String BROADCAST = BASE + "broadcast";
    public static final String MESSAGE = BASE + "message";
    public static final String MESSAGES_SPY = BASE + "messages.spy";
    public static final String MESSAGES_TOGGLE = BASE + "messages.toggle";
    public static final String MESSAGES_TOGGLE_OVERRIDE = BASE + "messages.toggle.override";
    public static final String CHANNEL = BASE + "channel";
    public static final String PROXYCHAT = BASE + "proxychat";

    private CommandPermissions() {
    }
}
