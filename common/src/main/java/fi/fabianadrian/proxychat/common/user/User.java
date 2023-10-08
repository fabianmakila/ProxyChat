package fi.fabianadrian.proxychat.common.user;

import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.platform.PlatformPlayer;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class User implements Commander {

	private final transient PlatformPlayer player;

	private boolean announcements = true;
	private MessageSettings messageSettings = new MessageSettings();
	private Set<String> mutedChannels = new HashSet<>();
	private transient UUID lastMessaged;
	private Set<UUID> blockedUsers = new HashSet<>();

	public User(PlatformPlayer player) {
		this.player = player;
	}

	public void populate(User deserialized) {
		this.announcements = deserialized.announcements;
		this.messageSettings = deserialized.messageSettings;
		this.mutedChannels = deserialized.mutedChannels;
		this.blockedUsers = deserialized.blockedUsers;
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

	public MessageSettings messageSettings() {
		return this.messageSettings;
	}

	public UUID lastMessaged() {
		return this.lastMessaged;
	}

	public void lastMessaged(UUID uuid) {
		this.lastMessaged = uuid;
	}

	public boolean addBlockedUser(User user) {
		return this.blockedUsers.add(user.uuid());
	}

	public boolean removeBlockedUser(User user) {
		return this.blockedUsers.remove(user.uuid());
	}

	public Set<UUID> blockedUsers() {
		return Set.copyOf(blockedUsers);
	}

	@Override
	public @NotNull Audience audience() {
		return this.player;
	}
}
