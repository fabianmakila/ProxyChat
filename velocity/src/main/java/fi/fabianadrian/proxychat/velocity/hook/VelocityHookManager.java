package fi.fabianadrian.proxychat.velocity.hook;

import de.simonsator.partyandfriends.velocity.api.pafplayers.PAFPlayerManager;
import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;

public class VelocityHookManager extends HookManager {
    private final FriendHook friendHook;

    public VelocityHookManager() {
        PAFPlayerManager pafPlayerManager = PAFPlayerManager.getInstance();
        if (pafPlayerManager != null) {
            this.friendHook = new PAFVelocityFriendHook(pafPlayerManager);
        } else {
            this.friendHook = FriendHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }
}
