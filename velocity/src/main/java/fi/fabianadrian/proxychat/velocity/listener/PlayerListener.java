package fi.fabianadrian.proxychat.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.velocity.ProxyChatVelocity;

import java.util.*;

public final class PlayerListener {
	private final ProxyChat proxyChat;
	private final ProxyChatVelocity plugin;

	public PlayerListener(ProxyChatVelocity plugin, ProxyChat proxyChat) {
		this.plugin = plugin;
		this.proxyChat = proxyChat;
	}

	@Subscribe
	public void onChat(PlayerChatEvent event) {
		if (!this.proxyChat.configManager().mainConfig().globalChat()) {
			return;
		}

		Optional<ServerConnection> currentServerOptional = event.getPlayer().getCurrentServer();
		if (currentServerOptional.isEmpty()) {
			return;
		}

		Collection<Player> currentServerPlayers = currentServerOptional.get().getServer().getPlayersConnected();
		List<UUID> recipientUUIDList = new ArrayList<>();
		this.plugin.server().getAllPlayers().forEach(player -> {
			if (currentServerPlayers.contains(player)) {
				return;
			}
			recipientUUIDList.add(player.getUniqueId());
		});

		this.proxyChat.messageService().sendGlobalMessage(event.getPlayer().getUniqueId(), event.getMessage(), recipientUUIDList);
	}
}
