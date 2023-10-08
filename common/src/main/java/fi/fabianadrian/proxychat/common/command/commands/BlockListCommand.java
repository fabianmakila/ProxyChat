package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.service.NameService;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockListCommand extends ProxyChatCommand {
	private final NameService nameService;

	public BlockListCommand(ProxyChat proxyChat) {
		super(proxyChat, "blocklist", "blocked", "ignorelist", "ignored");
		this.nameService = proxyChat.nameService();
	}

	@Override
	public void register() {
		this.manager.command(
				builder().handler(this::executeBlockList)
		);
	}

	private void executeBlockList(CommandContext<Commander> ctx) {
		User user = (User) ctx.getSender();

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
				resolvedNameComponents.add(Component.translatable("proxychat.general.unknown").hoverEvent(Component.text(uuid.toString())));
			}
		});

		user.sendMessage(Component.translatable("proxychat.command.blocklist.header", NamedTextColor.RED).appendNewline().color(NamedTextColor.WHITE).append(Component.join(JoinConfiguration.commas(true), resolvedNameComponents)));
	}
}
