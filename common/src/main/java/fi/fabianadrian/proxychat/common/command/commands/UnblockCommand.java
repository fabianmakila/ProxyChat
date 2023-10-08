package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.argument.UserArgument;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class UnblockCommand extends ProxyChatCommand {
	public UnblockCommand(ProxyChat proxyChat) {
		super(proxyChat, "unblock", "unignore");
	}

	@Override
	public void register() {
		this.manager.command(
				builder().argument(UserArgument.of("user")).handler(this::executeUnblock)
		);
	}

	private void executeUnblock(CommandContext<Commander> ctx) {
		User sender = (User) ctx.getSender();
		User target = ctx.get("user");

		if (target == sender) {
			sender.sendMessage(Component.translatable(
					"proxychat.command.unblock.self",
					NamedTextColor.RED
			));
			return;
		}

		if (sender.removeBlockedUser(target)) {
			sender.sendMessage(Component.translatable(
					"proxychat.command.unblock.success",
					NamedTextColor.GREEN
			).args(Component.text(target.name())));
		} else {
			sender.sendMessage(Component.translatable(
					"proxychat.command.unblock.not-blocked",
					NamedTextColor.RED
			).args(Component.text(target.name())));
		}
	}
}
