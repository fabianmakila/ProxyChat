package fi.fabianadrian.proxychat.bungeecord.listener;

import fi.fabianadrian.proxychat.bungeecord.BungeecordPlatformPlayer;
import fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord;
import fi.fabianadrian.proxychat.common.ProxyChat;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class LoginDisconnectListener implements Listener {

	private final ProxyChatBungeecord plugin;
	private final ProxyChat proxyChat;

	public LoginDisconnectListener(ProxyChatBungeecord plugin) {
		this.plugin = plugin;
		this.proxyChat = plugin.proxyChat();
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		BungeecordPlatformPlayer platformPlayer = new BungeecordPlatformPlayer(event.getPlayer(), this.plugin.adventure().player(event.getPlayer()));
		this.proxyChat.handleLogin(platformPlayer);
	}

	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent event) {
		this.proxyChat.handleDisconnect(event.getPlayer().getUniqueId());
	}
}
