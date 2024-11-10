package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.service.NameService;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.*;

public final class BlockListCommand extends ProxyChatCommand {
	private static final Component COMPONENT_BLOCKLIST_EMPTY = translatable()
			.key("proxychat.command.blocklist.empty")
			.color(NamedTextColor.RED)
			.build();
	private static final Component COMPONENT_BLOCKLIST_HEADER = translatable()
			.key("proxychat.command.blocklist.header")
			.color(NamedTextColor.RED)
			.appendNewline()
			.build();


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
			user.sendMessage(COMPONENT_BLOCKLIST_EMPTY);
			return;
		}

		List<Component> resolvedNameComponents = new ArrayList<>();
		user.blockedUsers().forEach(uuid -> {
			Optional<String> nameOptional = this.nameService.resolve(uuid);
			if (nameOptional.isPresent()) {
				resolvedNameComponents.add(text(nameOptional.get()));
			} else {
				resolvedNameComponents.add(Messages.GENERAL_UNKNOWN.hoverEvent(text(uuid.toString())));
			}
		});

		user.sendMessage(COMPONENT_BLOCKLIST_HEADER.append(
				join(JoinConfiguration.commas(true), resolvedNameComponents)).color(NamedTextColor.WHITE)
		);
	}
}
