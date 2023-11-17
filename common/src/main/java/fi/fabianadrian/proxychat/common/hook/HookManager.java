package fi.fabianadrian.proxychat.common.hook;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public abstract class HookManager {
	protected final Logger logger;
	protected FriendPluginHook friendPluginHook;
	protected VanishPluginHook vanishPluginHook;

	public HookManager(Logger logger) {
		this.logger = logger;
	}

	public void initialize() {
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
