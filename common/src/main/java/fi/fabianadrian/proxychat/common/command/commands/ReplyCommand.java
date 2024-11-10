package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.Optional;
import java.util.UUID;

import static net.kyori.adventure.text.Component.translatable;

public final class ReplyCommand extends ProxyChatCommand {
	private static final Component COMPONENT_ERROR_NO_LAST_MESSAGED = translatable(
			"proxychat.command.reply.no-last-messaged",
			NamedTextColor.RED
	);
	private static final Component COMPONENT_ERROR_LAST_MESSAGED_OFFLINE = translatable(
			"proxychat.command.reply.offline",
			NamedTextColor.RED
	);

	public ReplyCommand(ProxyChat proxyChat) {
		super(proxyChat, "reply", "r");
	}

	@Override
	public void register() {
		var builder = this.builder()
				.senderType(User.class)
				.required("message", StringParser.greedyStringParser())
				.handler(this::executeReply);

		this.manager.command(builder);
	}

	private void executeReply(CommandContext<User> ctx) {
		User user = ctx.sender();

		UUID lastMessaged = user.lastMessaged();
		if (lastMessaged == null) {
			user.sendMessage(COMPONENT_ERROR_NO_LAST_MESSAGED);
			return;
		}

		Optional<User> receiver = this.proxyChat.userManager().user(lastMessaged);
		if (receiver.isEmpty()) {
			user.sendMessage(COMPONENT_ERROR_LAST_MESSAGED_OFFLINE);
			return;
		}

		this.proxyChat.messageService().sendPrivateMessage(user, receiver.get(), ctx.get("message"));
	}
}
