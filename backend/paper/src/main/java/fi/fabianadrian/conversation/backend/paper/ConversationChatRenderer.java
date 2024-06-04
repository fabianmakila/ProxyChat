package fi.fabianadrian.conversation.backend.paper;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConversationChatRenderer implements ChatRenderer {
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final ConversationPaper plugin;

	public ConversationChatRenderer(ConversationPaper plugin) {
		this.plugin = plugin;
	}

	@Override
	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
		TagResolver.Builder resolverBuilder = TagResolver.builder().resolvers(
				Placeholder.component("name", source.name()),
				Placeholder.component("display_name", sourceDisplayName),
				Placeholder.component("message", message)
		);

		if (this.plugin.dependencyManager().isMiniPlaceholdersPresent()) {
			resolverBuilder = resolverBuilder.resolver(MiniPlaceholders.getRelationalGlobalPlaceholders(source, viewer));
		}

		return miniMessage.deserialize(
				this.plugin.conversation().configManager().config().format(),
				resolverBuilder.build()
		);
	}
}
