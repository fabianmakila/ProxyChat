package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import org.incendo.cloud.context.CommandContext;

public final class RootCommand extends ProxyChatCommand {
	public RootCommand(ProxyChat proxyChat) {
		super(proxyChat, "proxychat");
	}

	@Override
	public void register() {
		this.manager.command(
				subCommand("reload").handler(this::executeReload)
		);

		var debugBuilder = subCommand("debug");
		this.manager.command(
				debugBuilder.literal("announcement")
						.handler(this::executeDebugAnnouncement)
		);
	}

	private void executeReload(CommandContext<Commander> ctx) {
		this.proxyChat.reload();
		ctx.sender().sendMessage(Messages.COMMAND_PROXYCHAT_RELOAD_SUCCESS);
	}

	private void executeDebugAnnouncement(CommandContext<Commander> ctx) {
		this.proxyChat.announcementService().sendAnnouncement();
	}
}
