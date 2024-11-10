package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.parser.UserParser;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

import static net.kyori.adventure.text.Component.translatable;

public final class BlockCommand extends ProxyChatCommand {
	private static final TranslatableComponent.Builder COMPONENT_BLOCK_SUCCESS = translatable()
			.key("proxychat.command.block.success")
			.color(NamedTextColor.GREEN);
	private static final TranslatableComponent.Builder COMPONENT_BLOCK_ALREADY_BLOCKED = translatable()
			.key("proxychat.command.block.already-blocked")
			.color(NamedTextColor.RED);

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
			sender.sendMessage(COMPONENT_BLOCK_SUCCESS.arguments(target.nameAsComponent()));
		} else {
			sender.sendMessage(COMPONENT_BLOCK_ALREADY_BLOCKED.arguments(target.nameAsComponent()));
		}
	}
}
