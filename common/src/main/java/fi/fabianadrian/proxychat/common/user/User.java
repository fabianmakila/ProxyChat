package fi.fabianadrian.proxychat.common.user;

import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.command.Commander;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ConfigSerializable
public abstract class User<P> implements Commander {

    protected final transient P base;

    private boolean announcements = true;
    private boolean messages = true;
    private boolean spying;
    private Set<String> mutedChannels = new HashSet<>();
    private transient UUID lastMessaged;

    private transient String selectedChannel;

    public User(P base) {
        this.base = base;
    }

    public void populate(User<P> deserialized) {
        this.announcements = deserialized.announcements;
        this.messages = deserialized.messages;
        this.spying = deserialized.spying;
        this.mutedChannels = deserialized.mutedChannels;
    }

    public P base() {
        return this.base;
    }

    public abstract UUID uuid();

    public abstract String name();

    public boolean spying() {
        return this.spying;
    }

    public void spying(boolean value) {
        this.spying = value;
    }

    public Set<String> mutedChannels() {
        return Collections.unmodifiableSet(this.mutedChannels);
    }

    public boolean muteChannel(Channel channel) {
        return this.mutedChannels.add(channel.name());
    }

    public boolean unMuteChannel(Channel channel) {
        return this.mutedChannels.remove(channel.name());
    }

    public String selectedChannel() {
        return this.selectedChannel;
    }

    public void selectedChannel(Channel channel) {
        this.selectedChannel = channel.name();
    }

    public void clearSelectedChannel() {
        this.selectedChannel = null;
    }

    public boolean announcements() {
        return announcements;
    }

    public void announcements(boolean value) {
        this.announcements = value;
    }

    public boolean allowMessages() {
        return this.messages;
    }

    public void allowMessages(boolean value) {
        this.messages = value;
    }

    public UUID lastMessaged() {
        return this.lastMessaged;
    }

    public void lastMessaged(UUID uuid) {
        this.lastMessaged = uuid;
    }
}
