package fi.fabianadrian.proxychat.api.channel;

import java.util.List;

public interface Channel {
    String name();

    String format();

    String permission();

    String commandName();

    List<String> commandAliases();

    static boolean isValidName(String name) {
        return name.matches("^[\\w\\-_]+$");
    }
}
