package fi.fabianadrian.proxychat.common.dependency;

import fi.fabianadrian.conversation.common.dependency.DependencyManager;
import org.slf4j.Logger;

import java.util.Optional;

public abstract class ProxyDependencyManager extends DependencyManager {
	protected final Logger logger;
	protected FriendPluginHook friendPluginHook;
	protected VanishPluginHook vanishPluginHook;

	public ProxyDependencyManager(Logger logger) {
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

	public Optional<FriendPluginHook> friendHook() {
		return Optional.ofNullable(this.friendPluginHook);
	}

	public Optional<VanishPluginHook> vanishHook() {
		return Optional.ofNullable(this.vanishPluginHook);
	}

	protected abstract void initializeFriendHook();

	protected abstract void initializeVanishHook();
}
