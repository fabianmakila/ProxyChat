package fi.fabianadrian.proxychat.bungeecord.hook;

import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import fi.fabianadrian.proxychat.common.hook.FriendPluginHook;

import java.util.UUID;

public final class PAFBungeecordFriendHook implements FriendPluginHook {
	final PAFPlayerManager playerManager = PAFPlayerManager.getInstance();

	@Override
	public boolean areFriends(UUID uuid1, UUID uuid2) {
		return this.playerManager.getPlayer(uuid1).isAFriendOf(
				this.playerManager.getPlayer(uuid2)
		);
	}

	@Override
	public String pluginName() {
		return "PartyAndFriends";
	}
}
