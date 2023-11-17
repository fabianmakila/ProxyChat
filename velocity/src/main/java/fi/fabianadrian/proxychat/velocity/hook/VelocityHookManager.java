package fi.fabianadrian.proxychat.velocity.hook;

import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.velocity.ProxyChatVelocity;

public class VelocityHookManager extends HookManager {
	private final ProxyChatVelocity plugin;

	public VelocityHookManager(ProxyChatVelocity plugin) {
		super(plugin.logger());
		this.plugin = plugin;
	}

	@Override
	protected void initializeFriendHook() {
		if (isPluginPresent("partyandfriends")) {
			this.friendPluginHook = new PAFVelocityFriendHook();
		}
	}

	@Override
	protected void initializeVanishHook() {
		if (isPluginPresent("premiumvanish")) {
			this.vanishPluginHook = new PremiumVanishVelocityHook();
		}
	}

	private boolean isPluginPresent(String id) {
		return this.plugin.server().getPluginManager().isLoaded(id);
	}
}
