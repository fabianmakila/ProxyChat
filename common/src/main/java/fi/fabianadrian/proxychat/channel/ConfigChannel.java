package fi.fabianadrian.proxychat.channel;

import fi.fabianadrian.proxychat.api.channel.Channel;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Collections;
import java.util.List;

@ConfigSerializable
public final class ConfigChannel implements Channel {

    @Comment("The channel's name, visible in commands.")
    private String name = "example";
    @Comment("Chat format for the channel.\nPlaceholders: <sender>, <message>")
    private String format = "[Example] <sender>: <message>";
    @Comment("The command used to interact with the channel.")
    private String commandName = "example";
    @Comment("Additional command aliases.")
    private List<String> commandAliases = List.of("example-alias1", "example-alias2");

    public ConfigChannel() {
    }

    public ConfigChannel(String name) {
        if (!Channel.isValidName(name)) throw new IllegalStateException("Invalid channel name: " + name);
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String format() {
        return this.format;
    }

    @Override
    public String permission() {
        return "proxychat.channel." + name;
    }

    @Override
    public String commandName() {
        return this.commandName;
    }

    @Override
    public List<String> commandAliases() {
        return Collections.unmodifiableList(this.commandAliases);
    }
}
