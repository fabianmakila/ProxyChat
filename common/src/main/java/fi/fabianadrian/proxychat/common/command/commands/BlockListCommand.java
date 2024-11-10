package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.service.NameService;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BlockListCommand extends ProxyChatCommand {
	private final NameService nameService;

	public BlockListCommand(ProxyChat proxyChat) {
		super(proxyChat, "blocklist", "blocked", "ignorelist", "ignored");
		this.nameService = proxyChat.nameService();
	}

	@Override
	public void register() {
		this.manager.command(
				builder().senderType(User.class).handler(this::executeBlockList)
		);
	}

	private void executeBlockList(CommandContext<User> ctx) {
		User user = ctx.sender();

		if (user.blockedUsers().isEmpty()) {
			user.sendMessage(Component.translatable("proxychat.command.blocklist.empty", NamedTextColor.RED));
			return;
		}

		List<Component> resolvedNameComponents = new ArrayList<>();
		user.blockedUsers().forEach(uuid -> {
			Optional<String> nameOptional = this.nameService.resolve(uuid);
			if (nameOptional.isPresent()) {
				resolvedNameComponents.add(Component.text(nameOptional.get()));
			} else {
				resolvedNameComponents.add(Messages.GENERAL_UNKNOWN.hoverEvent(Component.text(uuid.toString())));
			}
		});

		user.sendMessage(Component.translatable("proxychat.command.blocklist.header", NamedTextColor.RED).appendNewline().append(Component.join(JoinConfiguration.commas(true), resolvedNameComponents)).color(NamedTextColor.WHITE));
	}
}
