package fi.fabianadrian.proxychat.velocity.hook;

import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.hook.vanish.VanishHook;
import org.slf4j.Logger;

public class VelocityHookManager extends HookManager {
    private FriendHook friendHook;
    private VanishHook vanishHook = VanishHook.empty();

    public VelocityHookManager(Logger logger) {
        super(logger);

        try {
            this.friendHook = new PAFVelocityFriendHook();
            this.logger.info("PartyAndFriends hook enabled!");
        } catch (NoClassDefFoundError e) {
            this.friendHook = FriendHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }

    @Override
    public VanishHook vanishHook() {
        return this.vanishHook;
    }
}
