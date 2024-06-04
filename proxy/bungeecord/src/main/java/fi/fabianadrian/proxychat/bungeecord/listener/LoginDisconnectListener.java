package fi.fabianadrian.proxychat.bungeecord.listener;

import fi.fabianadrian.proxychat.bungeecord.BungeecordPlatformPlayer;
import fi.fabianadrian.proxychat.bungeecord.ConversationBungeecord;
import fi.fabianadrian.proxychat.common.ConversationProxy;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class LoginDisconnectListener implements Listener {

	private final ConversationBungeecord plugin;
	private final ConversationProxy conversation;

	public LoginDisconnectListener(ConversationBungeecord plugin) {
		this.plugin = plugin;
		this.conversation = plugin.conversation();
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		BungeecordPlatformPlayer platformPlayer = new BungeecordPlatformPlayer(event.getPlayer(), this.plugin.adventure().player(event.getPlayer()));
		this.conversation.handleLogin(platformPlayer);
	}

	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent event) {
		this.conversation.handleDisconnect(event.getPlayer().getUniqueId());
	}
}
