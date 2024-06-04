package fi.fabianadrian.conversation.backend.paper.listener;

import fi.fabianadrian.conversation.backend.paper.ConversationChatRenderer;
import fi.fabianadrian.conversation.backend.paper.ConversationPaper;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ChatListener implements Listener {
	private final ConversationChatRenderer renderer;

	public ChatListener(ConversationPaper plugin) {
		this.renderer = new ConversationChatRenderer(plugin);
	}

	@EventHandler
	public void onChat(AsyncChatEvent event) {
		event.renderer(this.renderer);
	}
}
