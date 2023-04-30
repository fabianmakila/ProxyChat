package fi.fabianadrian.proxychat.bungeecord.hook;

import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import org.slf4j.Logger;

public class BungeecordHookManager extends HookManager {
    private final FriendHook friendHook;

    public BungeecordHookManager(Logger logger) {
        super(logger);

        PAFPlayerManager pafPlayerManager = PAFPlayerManager.getInstance();
        if (pafPlayerManager != null) {
            this.friendHook = new PAFBungeecordFriendHook(pafPlayerManager);
            this.logger.info("PartyAndFriends hook enabled!");
        } else {
            this.friendHook = FriendHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }
}
