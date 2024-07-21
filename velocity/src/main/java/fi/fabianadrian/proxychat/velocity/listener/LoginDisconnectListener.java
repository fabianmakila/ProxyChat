package fi.fabianadrian.proxychat.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.velocity.VelocityPlatformPlayer;

public final class LoginDisconnectListener {
	private final ProxyChat proxyChat;

	public LoginDisconnectListener(ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
	}

	@Subscribe
	public void onLogin(PostLoginEvent event) {
		VelocityPlatformPlayer platformPlayer = new VelocityPlatformPlayer(event.getPlayer());
		this.proxyChat.handleLogin(platformPlayer);
	}

	@Subscribe
	public void onDisconnect(DisconnectEvent event) {
		switch (event.getLoginStatus()) {
			case PRE_SERVER_JOIN:
			case SUCCESSFUL_LOGIN:
				this.proxyChat.handleDisconnect(event.getPlayer().getUniqueId());
		}
	}
}
