package fi.fabianadrian.proxychat.common.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import fi.fabianadrian.proxychat.common.ProxyChat;

public abstract class ProxyChatCommand {
	protected final ProxyChat proxyChat;
	protected final CommandManager<Commander> manager;
	private final String permission;
	private final Command.Builder<Commander> builder;

	public ProxyChatCommand(ProxyChat proxyChat, String commandName, String... commandAliases) {
		this.proxyChat = proxyChat;
		this.manager = proxyChat.platform().commandManager();

		this.permission = "proxychat.command." + commandName;

		this.builder = this.manager.commandBuilder(
				commandName,
				commandAliases
		);
	}

	public abstract void register();

	protected Command.Builder<Commander> builder() {
		return this.builder.permission(this.permission);
	}

	protected Command.Builder<Commander> subCommand(String literal) {
		return this.builder.literal(literal).permission(this.permission + "." + literal);
	}
}
