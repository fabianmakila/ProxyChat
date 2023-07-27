package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class BroadcastCommand extends ProxyChatCommand {
	private final MiniMessage miniMessage;

	public BroadcastCommand(ProxyChat proxyChat) {
		super(proxyChat, "broadcast", "bc");
		this.miniMessage = MiniMessage.miniMessage();
	}

	@Override
	public void register() {
		var builder = this.builder()
				.argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
				.handler(this::executeBroadcast);

		this.manager.command(builder);
	}

	private void executeBroadcast(CommandContext<Commander> ctx) {
		String message = ctx.get("message");
		this.proxyChat.platform().sendMessage(
				broadcastComponent(message)
		);
	}

	private Component broadcastComponent(String message) {
		String format = this.proxyChat.configManager().mainConfig().formats().broadcast();
		return this.miniMessage.deserialize(
				format,
				TagResolver.resolver(
						Placeholder.parsed("message", message)
				)
		);
	}
}
