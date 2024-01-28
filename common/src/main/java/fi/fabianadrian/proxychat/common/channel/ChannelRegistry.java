package fi.fabianadrian.proxychat.common.channel;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import com.sun.jdi.connect.Connector;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.user.User;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ChannelRegistry {

	private final ProxyChat proxyChat;
	private final Map<String, Channel> registry = new HashMap<>();

	public ChannelRegistry(ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
		loadChannels();
	}

	public Channel channel(String name) {
		return this.registry.get(name);
	}

	public Collection<Channel> channels() {
		return Collections.unmodifiableCollection(registry.values());
	}

	private void loadChannels() {
		this.proxyChat.configManager().channelsConfig().channels().forEach(channel -> {
			this.registry.put(channel.name(), channel);
			registerChannelCommand(channel);
		});
	}

	//TODO Maybe move most of this under command package???
	private void registerChannelCommand(Channel channel) {
		CommandManager<Commander> commandManager = this.proxyChat.platform().commandManager();
		var builder = commandManager.commandBuilder(
						channel.command(),
						channel.commandAliases(),
						commandManager.createDefaultCommandMeta()
				)
				.permission(channel.permission())
				.required("message", StringParser.greedyStringParser())
				.senderType(User.class)
				.handler(handler -> {
					final String message = handler.get("message");
					this.proxyChat.messageService().sendChannelMessage(channel, handler.sender(), message);
				});

		commandManager.command(builder);
	}
}
