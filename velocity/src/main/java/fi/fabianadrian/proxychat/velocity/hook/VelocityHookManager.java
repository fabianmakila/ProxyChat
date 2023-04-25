package fi.fabianadrian.proxychat.velocity.hook;

import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;

public class VelocityHookManager extends HookManager {
    private FriendHook friendHook;

    public VelocityHookManager() {
        try {
            this.friendHook = new PAFVelocityFriendHook();
        } catch (NoClassDefFoundError e) {
            this.friendHook = FriendHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }
}
