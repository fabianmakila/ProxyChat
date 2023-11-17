package fi.fabianadrian.proxychat.common.hook;

import java.util.UUID;

public interface FriendPluginHook extends PluginHook {
	boolean areFriends(UUID uuid1, UUID uuid2);
}
