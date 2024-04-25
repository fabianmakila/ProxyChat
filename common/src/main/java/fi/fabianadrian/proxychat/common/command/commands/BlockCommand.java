package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.parser.UserParser;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

public final class BlockCommand extends ProxyChatCommand {
	public BlockCommand(ProxyChat proxyChat) {
		super(proxyChat, "block", "ignore");
	}

	@Override
	public void register() {
		this.manager.command(
				//TODO Allow blocking offline players
				builder().senderType(User.class).required("player", UserParser.userParser()).handler(this::executeBlock)
		);
	}

	public void executeBlock(CommandContext<User> ctx) {
		User sender = ctx.sender();
		User target = ctx.get("player");

		if (sender.addBlockedUser(target)) {
			sender.sendMessage(Component.translatable(
					"proxychat.command.block.success",
					NamedTextColor.GREEN
			).arguments(Component.text(target.name())));
		} else {
			sender.sendMessage(Component.translatable(
					"proxychat.command.block.already-blocked",
					NamedTextColor.RED
			).arguments(Component.text(target.name())));
		}
	}
}
