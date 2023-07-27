package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.argument.UserArgument;
import fi.fabianadrian.proxychat.common.user.User;

public final class MessageCommand extends ProxyChatCommand {
	public MessageCommand(ProxyChat proxyChat) {
		super(proxyChat, "message", "msg", "dm");
	}

	@Override
	public void register() {
		var builder = this.builder()
				.senderType(User.class)
				.argument(UserArgument.of("receiver"))
				.argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
				.handler(this::executeMessage);

		this.manager.command(builder);
	}

	private void executeMessage(CommandContext<Commander> ctx) {
		this.proxyChat.messageService().sendPrivateMessage((User) ctx.getSender(), ctx.get("receiver"), ctx.get("message"));
	}
}
