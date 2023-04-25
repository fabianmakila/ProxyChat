package fi.fabianadrian.proxychat.velocity.hook;

import de.simonsator.partyandfriends.velocity.api.pafplayers.PAFPlayerManager;
import fi.fabianadrian.proxychat.common.hook.FriendHook;

import java.util.UUID;

public class PAFVelocityFriendHook implements FriendHook {
    final PAFPlayerManager playerManager = PAFPlayerManager.getInstance();

    @Override
    public boolean areFriends(UUID uuid1, UUID uuid2) {
        return this.playerManager.getPlayer(uuid1).isAFriendOf(
            this.playerManager.getPlayer(uuid2)
        );
    }
}
