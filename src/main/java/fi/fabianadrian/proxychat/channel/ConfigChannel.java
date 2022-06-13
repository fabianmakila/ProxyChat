package fi.fabianadrian.proxychat.channel;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
public final class ConfigChannel implements Channel {

    @Comment("The channel's name, visible in commands.")
    private String name;
    private String format = "<sender> <message>";
    private String commandName;
    private List<String> commandAliases = new ArrayList<>();

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
