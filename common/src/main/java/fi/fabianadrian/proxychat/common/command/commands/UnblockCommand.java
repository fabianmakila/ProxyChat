package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.parser.UserParser;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

public final class UnblockCommand extends ProxyChatCommand {
	private static final TranslatableComponent.Builder COMPONENT_BUILDER_SUCCESS = Component.translatable()
			.key("proxychat.command.unblock.success")
			.color(NamedTextColor.GREEN);
	private static final TranslatableComponent.Builder COMPONENT_BUILDER_NOT_BLOCKED = Component.translatable()
			.key("proxychat.command.unblock.not-blocked")
			.color(NamedTextColor.RED);

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
			sender.sendMessage(COMPONENT_BUILDER_SUCCESS.arguments(target.nameAsComponent()));
		} else {
			sender.sendMessage(COMPONENT_BUILDER_NOT_BLOCKED.arguments(target.nameAsComponent()));
		}
	}
}
