package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

import static net.kyori.adventure.text.Component.translatable;

public final class RootCommand extends ProxyChatCommand {
	private static final Component COMPONENT_RELOAD_SUCCESS = translatable(
			"proxychat.command.proxychat.reload.success",
			NamedTextColor.GREEN
	);

	public RootCommand(ProxyChat proxyChat) {
		super(proxyChat, "proxychat");
	}

	@Override
	public void register() {
		this.manager.command(
				subCommand("reload").handler(this::executeReload)
		);

		var debugBuilder = subCommand("debug");
		this.manager.command(debugBuilder.literal("announcement")
				.handler(this::executeDebugAnnouncement)
		);
	}

	private void executeReload(CommandContext<Commander> ctx) {
		this.proxyChat.reload();
		ctx.sender().sendMessage(COMPONENT_RELOAD_SUCCESS);
	}

	private void executeDebugAnnouncement(CommandContext<Commander> ctx) {
		this.proxyChat.announcementService().sendAnnouncement();
	}
}
