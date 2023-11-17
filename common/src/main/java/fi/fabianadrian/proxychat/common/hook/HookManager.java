package fi.fabianadrian.proxychat.common.hook;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public abstract class HookManager {
	protected FriendPluginHook friendPluginHook;
	protected VanishPluginHook vanishPluginHook;
	protected final Logger logger;

	public HookManager(Logger logger) {
		this.logger = logger;

		initializeFriendHook();
		initializeVanishHook();

		if (this.friendPluginHook != null) {
			this.logger.info(String.format("Hooked into %s.", this.friendPluginHook.pluginName()));
		}
		if (this.vanishPluginHook != null) {
			this.logger.info(String.format("Hooked into %s.", this.vanishPluginHook.pluginName()));
		}
	}

	public @Nullable FriendPluginHook friendHook() {
		return this.friendPluginHook;
	}

	public @Nullable VanishPluginHook vanishHook() {
		return this.vanishPluginHook;
	}

	protected abstract void initializeFriendHook();

	protected abstract void initializeVanishHook();
}
