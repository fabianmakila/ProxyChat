package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.argument.UserArgument;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BlockCommand extends ProxyChatCommand {
	public BlockCommand(ProxyChat proxyChat) {
		super(proxyChat, "block", "ignore");
	}

	@Override
	public void register() {
		this.manager.command(
				builder().argument(UserArgument.of("player")).handler(this::executeBlock)
		);
	}

	public void executeBlock(CommandContext<Commander> ctx) {
		User sender = (User) ctx.getSender();
		User target = ctx.get("player");

		if (sender.addBlockedUser(target)) {
			sender.sendMessage(Component.translatable(
					"proxychat.command.block.success",
					NamedTextColor.GREEN
			).args(Component.text(target.name())));
		} else {
			sender.sendMessage(Component.translatable(
					"proxychat.command.block.already-blocked",
					NamedTextColor.RED
			).args(Component.text(target.name())));
		}
	}
}
