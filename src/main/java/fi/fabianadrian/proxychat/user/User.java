package fi.fabianadrian.proxychat.user;

import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.channel.Channel;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ConfigSerializable
public final class User {

    private transient Player player;

    private boolean announcements = true;
    private boolean messages = true;
    private boolean spying;
    private Set<String> mutedChannels = new HashSet<>();
    private transient UUID lastMessaged;

    private transient String selectedChannel;

    public Player player() {
        return this.player;
    }

    public void player(Player player) {
        this.player = player;
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

    public boolean messages() {
        return this.messages;
    }

    public void messages(boolean value) {
        this.messages = value;
    }

    public UUID lastMessaged() {
        return this.lastMessaged;
    }

    public void lastMessaged(UUID uuid) {
        this.lastMessaged = uuid;
    }
}
