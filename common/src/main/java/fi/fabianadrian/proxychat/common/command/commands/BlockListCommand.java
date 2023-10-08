package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;

public class BlockListCommand extends ProxyChatCommand {
	public BlockListCommand(ProxyChat proxyChat) {
		super(proxyChat, "blocklist", "blocked", "ignorelist", "ignored");
	}

	@Override
	public void register() {
		this.manager.command(
				builder().handler(this::executeBlockList)
		);
	}

	private void executeBlockList(CommandContext<Commander> ctx) {

	}
}
