package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.argument.UserArgument;
import fi.fabianadrian.proxychat.common.user.User;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;

public final class MessageCommand extends ProxyChatCommand {
	public MessageCommand(ProxyChat proxyChat) {
		super(proxyChat, "message", "msg", "dm");
	}

	@Override
	public void register() {
		var builder = this.builder()
				.senderType(User.class)
				.argument(UserArgument.of("receiver"))
				.required("message", StringParser.greedyStringParser())
				.handler(this::executeMessage);

		this.manager.command(builder);
	}

	private void executeMessage(CommandContext<User> ctx) {
		this.proxyChat.messageService().sendPrivateMessage(ctx.sender(), ctx.get("receiver"), ctx.get("message"));
	}
}
