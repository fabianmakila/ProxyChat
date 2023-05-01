package fi.fabianadrian.proxychat.common.hook;

import fi.fabianadrian.proxychat.common.hook.vanish.PremiumVanishHook;
import fi.fabianadrian.proxychat.common.hook.vanish.VanishHook;
import org.slf4j.Logger;

public abstract class HookManager {

    protected final Logger logger;
    protected VanishHook vanishHook;

    public HookManager(Logger logger) {
        this.logger = logger;

        try {
            Class.forName("de.myzelyam.api.vanish.VanishAPI");
            this.vanishHook = new PremiumVanishHook();
        } catch (ClassNotFoundException e) {
            this.vanishHook = VanishHook.empty();
        }
    }

    public abstract FriendHook friendHook();

    public VanishHook vanishHook() {
        return this.vanishHook;
    }
}
