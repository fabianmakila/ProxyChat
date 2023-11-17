package fi.fabianadrian.proxychat.bungeecord.hook;

import fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord;
import fi.fabianadrian.proxychat.common.hook.HookManager;

public class BungeecordHookManager extends HookManager {
	private final ProxyChatBungeecord plugin;

	public BungeecordHookManager(ProxyChatBungeecord plugin) {
		super(plugin.logger());
		this.plugin = plugin;
	}

	@Override
	protected void initializeFriendHook() {
		if (isPluginPresent("PartyAndFriends")) {
			this.friendPluginHook = new PAFBungeecordFriendHook();
		}
	}

	@Override
	protected void initializeVanishHook() {
		if (isPluginPresent("PremiumVanish")) {
			this.vanishPluginHook = new PremiumVanishBungeecordHook();
		}
	}

	private boolean isPluginPresent(String name) {
		return this.plugin.getProxy().getPluginManager().getPlugin(name) != null;
	}
}
