package fi.fabianadrian.proxychat.common.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.ArgumentParseException;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.argument.ChannelArgument;
import fi.fabianadrian.proxychat.common.command.argument.UserArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.ComponentMessageThrowable;

public class ProxyChatExceptionHandler {

	public ProxyChatExceptionHandler(ProxyChat proxyChat) {
		CommandManager<Commander> manager = proxyChat.platform().commandManager();

		manager.registerExceptionHandler(
				InvalidSyntaxException.class,
				(commander, exception) -> commander.sendMessage(
						Component.translatable("proxychat.command.error.syntax", NamedTextColor.RED).args(Component.text(exception.getCorrectSyntax(), NamedTextColor.WHITE))
				)
		);

		manager.registerExceptionHandler(
				ArgumentParseException.class,
				(commander, exception) -> {
					Throwable cause = exception.getCause();

					if (cause instanceof ChannelArgument.ChannelParseException) {
						commander.sendMessage(Component.translatable("proxychat.command.error.channel", NamedTextColor.RED));
						return;
					}

					if (cause instanceof UserArgument.UserParseException) {
						commander.sendMessage(Component.translatable("proxychat.command.error.user", NamedTextColor.RED));
						return;
					}

					commander.sendMessage(
							Component.text("Invalid command argument: ", NamedTextColor.RED).append(getMessage(cause).colorIfAbsent(NamedTextColor.WHITE))
					);
				}
		);
	}

	private static Component getMessage(final Throwable throwable) {
		final Component msg = ComponentMessageThrowable.getOrConvertMessage(throwable);
		return msg == null ? Component.text("null") : msg;
	}
}
