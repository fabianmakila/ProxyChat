package fi.fabianadrian.proxychat.common.user;

import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.command.Commander;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class User implements Commander {

    private final transient PlatformPlayer player;

    private boolean announcements = true;
    private MessageSetting messageSetting = MessageSetting.EVERYONE;
    private boolean spying;
    private Set<String> mutedChannels = new HashSet<>();
    private transient UUID lastMessaged;

    public User(PlatformPlayer player) {
        this.player = player;
    }

    public void populate(User deserialized) {
        this.announcements = deserialized.announcements;
        this.messageSetting = deserialized.messageSetting;
        this.spying = deserialized.spying;
        this.mutedChannels = deserialized.mutedChannels;
    }

    public PlatformPlayer player() {
        return this.player;
    }

    public UUID uuid() {
        return this.player.uuid();
    }

    public String name() {
        return this.player.name();
    }

    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

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

    public boolean announcements() {
        return announcements;
    }

    public void announcements(boolean value) {
        this.announcements = value;
    }

    public MessageSetting messageSetting() {
        return this.messageSetting;
    }

    public void messageSetting(MessageSetting value) {
        this.messageSetting = value;
    }

    public UUID lastMessaged() {
        return this.lastMessaged;
    }

    public void lastMessaged(UUID uuid) {
        this.lastMessaged = uuid;
    }

    @Override
    public @NotNull Audience audience() {
        return this.player;
    }

    public enum MessageSetting {
        NOBODY, FRIENDS, EVERYONE
    }
}
