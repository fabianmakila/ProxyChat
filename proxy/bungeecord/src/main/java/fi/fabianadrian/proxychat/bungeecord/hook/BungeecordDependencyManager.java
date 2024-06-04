package fi.fabianadrian.proxychat.bungeecord.hook;

import fi.fabianadrian.proxychat.bungeecord.ConversationBungeecord;
import fi.fabianadrian.proxychat.common.dependency.ProxyDependencyManager;

public final class BungeecordDependencyManager extends ProxyDependencyManager {
	private final ConversationBungeecord plugin;

	public BungeecordDependencyManager(ConversationBungeecord plugin) {
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

	@Override
	public boolean isMiniPlaceholdersPresent() {
		return false;
	}
}
