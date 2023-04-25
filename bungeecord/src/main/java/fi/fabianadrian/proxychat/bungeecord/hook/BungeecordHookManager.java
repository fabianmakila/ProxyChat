package fi.fabianadrian.proxychat.bungeecord.hook;

import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;

public class BungeecordHookManager extends HookManager {
    private final FriendHook friendHook;

    public BungeecordHookManager() {
        PAFPlayerManager pafPlayerManager = PAFPlayerManager.getInstance();
        if (pafPlayerManager != null) {
            this.friendHook = new PAFBungeecordFriendHook(pafPlayerManager);
        } else {
            this.friendHook = FriendHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }
}
