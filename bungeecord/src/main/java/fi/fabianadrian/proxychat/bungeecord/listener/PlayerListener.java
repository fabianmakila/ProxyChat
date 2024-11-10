package fi.fabianadrian.proxychat.bungeecord.listener;

import fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord;
import fi.fabianadrian.proxychat.common.ProxyChat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class PlayerListener implements Listener {
	private final ProxyChatBungeecord plugin;
	private final ProxyChat proxyChat;

	public PlayerListener(ProxyChatBungeecord plugin) {
		this.plugin = plugin;
		this.proxyChat = plugin.proxyChat();
	}

	@EventHandler
	public void onChat(ChatEvent event) {
		if (!this.proxyChat.configManager().mainConfig().globalChat()) {
			return;
		}

		if (!(event.getSender() instanceof ProxiedPlayer sender)) {
			return;
		}

		Collection<ProxiedPlayer> currentServerPlayers = sender.getServer().getInfo().getPlayers();
		List<UUID> recipientUUIDs = new ArrayList<>();

		this.plugin.getProxy().getPlayers().forEach(player -> {
			if (currentServerPlayers.contains(player)) {
				return;
			}
			recipientUUIDs.add(player.getUniqueId());
		});

		this.proxyChat.messageService().sendGlobalMessage(sender.getUniqueId(), event.getMessage(), recipientUUIDs);
	}
}
