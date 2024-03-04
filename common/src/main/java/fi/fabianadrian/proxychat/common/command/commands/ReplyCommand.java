package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.Optional;
import java.util.UUID;

public final class ReplyCommand extends ProxyChatCommand {
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
			user.sendMessage(Messages.COMMAND_REPLY_ERROR_NO_LAST_MESSAGED);
			return;
		}

		Optional<User> receiver = this.proxyChat.userManager().user(lastMessaged);
		if (receiver.isEmpty()) {
			user.sendMessage(Messages.COMMAND_REPLY_ERROR_LAST_MESSAGED_OFFLINE);
			return;
		}

		this.proxyChat.messageService().sendPrivateMessage(user, receiver.get(), ctx.get("message"));
	}
}
