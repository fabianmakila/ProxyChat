package fi.fabianadrian.conversation.backend.paper.listener;

import fi.fabianadrian.conversation.backend.paper.ConversationPaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerListener implements Listener {
	private final ConversationPaper plugin;

	public ServerListener(ConversationPaper plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onServerLoad(ServerLoadEvent event) {
		if (this.plugin.getServer().getPluginManager().isPluginEnabled("MiniPlaceholders")) {
			this.plugin.dependencyManager().markMiniPlaceholdersPresent();
		}
	}
}
