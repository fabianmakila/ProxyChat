package fi.fabianadrian.proxychat.bungeecord.hook;

import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import fi.fabianadrian.proxychat.common.hook.FriendHook;

import java.util.UUID;

public class PAFBungeecordFriendHook implements FriendHook {
    final PAFPlayerManager playerManager;

    public PAFBungeecordFriendHook(PAFPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean areFriends(UUID uuid1, UUID uuid2) {
        return this.playerManager.getPlayer(uuid1).isAFriendOf(
            this.playerManager.getPlayer(uuid2)
        );
    }
}
