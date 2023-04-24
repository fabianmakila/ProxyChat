package fi.fabianadrian.proxychat.common.channel;

import java.util.List;
import java.util.Locale;

public interface Channel {
    static Channel of(String name, String format, List<String> commandAliases) {
        return new Channel() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public String format() {
                return format;
            }

            @Override
            public List<String> commandAliases() {
                return commandAliases;
            }
        };
    }

    String name();

    String format();

    List<String> commandAliases();

    default String permission() {
        return "proxychat.channel." + name().toLowerCase(Locale.ROOT);
    }
}
