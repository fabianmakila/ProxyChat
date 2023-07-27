package fi.fabianadrian.proxychat.common.hook;

import fi.fabianadrian.proxychat.common.hook.vanish.VanishHook;
import org.slf4j.Logger;

public abstract class HookManager {

	protected final Logger logger;

	public HookManager(Logger logger) {
		this.logger = logger;
	}

	public abstract FriendHook friendHook();

	public abstract VanishHook vanishHook();
}
