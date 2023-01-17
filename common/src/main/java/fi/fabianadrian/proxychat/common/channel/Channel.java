package fi.fabianadrian.proxychat.common.channel;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Collections;
import java.util.List;

@ConfigSerializable
public final class Channel {

    @Comment("The channel's name, visible in commands.")
    private String name = "example";
    @Comment("Chat format for the channel.\nPlaceholders: <sender>, <message>")
    private String format = "[Example] <sender>: <message>";
    @Comment("The command used to interact with the channel.")
    private String commandName = "example";
    @Comment("Additional command aliases.")
    private List<String> commandAliases = List.of("example-alias1", "example-alias2");

    public Channel() {
    }

    public Channel(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public String format() {
        return this.format;
    }

    public String permission() {
        return "proxychat.channel." + name;
    }

    public String commandName() {
        return this.commandName;
    }

    public List<String> commandAliases() {
        return Collections.unmodifiableList(this.commandAliases);
    }
}
