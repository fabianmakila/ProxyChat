package fi.fabianadrian.proxychat.common.hook;

import org.slf4j.Logger;

public abstract class HookManager {

    protected final Logger logger;

    public HookManager(Logger logger) {
        this.logger = logger;
    }

    public abstract FriendHook friendHook();
}
