package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.parser.UserParser;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

public class UnblockCommand extends ProxyChatCommand {
	public UnblockCommand(ProxyChat proxyChat) {
		super(proxyChat, "unblock", "unignore");
	}

	@Override
	public void register() {
		this.manager.command(
				builder().senderType(User.class).required("player", UserParser.userParser()).handler(this::executeUnblock)
		);
	}

	private void executeUnblock(CommandContext<User> ctx) {
		User sender = ctx.sender();
		User target = ctx.get("player");

		if (sender.removeBlockedUser(target)) {
			sender.sendMessage(Component.translatable(
					"proxychat.command.unblock.success",
					NamedTextColor.GREEN
			).arguments(Component.text(target.name())));
		} else {
			sender.sendMessage(Component.translatable(
					"proxychat.command.unblock.not-blocked",
					NamedTextColor.RED
			).arguments(Component.text(target.name())));
		}
	}
}
