package fi.fabianadrian.proxychat.bungeecord.hook;

import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import org.slf4j.Logger;

public class BungeecordHookManager extends HookManager {
    private FriendHook friendHook;

    public BungeecordHookManager(Logger logger) {
        super(logger);

        try {
            this.friendHook = new PAFBungeecordFriendHook();
            this.logger.info("PartyAndFriends hook enabled!");
        } catch (NoClassDefFoundError e) {
            this.friendHook = FriendHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }
}
