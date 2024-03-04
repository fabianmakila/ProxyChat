package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.parser.ChannelParser;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ChannelCommand extends ProxyChatCommand {
	public ChannelCommand(ProxyChat proxyChat) {
		super(proxyChat, "channel", "ch");
	}

	@Override
	public void register() {
		this.manager.command(
				this.subCommand("list")
						.handler(this::executeList)
		);

		this.manager.command(
				this.subCommand("mute")
						.required("channel", ChannelParser.channelParser())
						.senderType(User.class)
						.handler(this::executeMute)
		);

		this.manager.command(
				this.subCommand("unmute")
						.required("channel", ChannelParser.channelParser())
						.senderType(User.class)
						.handler(this::executeUnmute)
		);
	}

	//TODO Maybe move some of this logic to elsewhere? Seems kinda messy
	private void executeList(CommandContext<Commander> ctx) {
		Stream<Channel> channels = this.proxyChat.channelRegistry().channels().stream().filter(channel -> ctx.sender().hasPermission(channel.permission()));
		List<Component> channelComponents = new ArrayList<>();

		if ((ctx.sender() instanceof User user)) {
			channels.forEach(channel -> channelComponents.add(user.mutedChannels().contains(channel.name()) ? Component.text(channel.name(), NamedTextColor.DARK_GRAY) : Component.text(channel.name(), NamedTextColor.GREEN)));
		} else {
			channels.forEach(channel -> channelComponents.add(Component.text(channel.name(), NamedTextColor.GREEN)));
		}

		ctx.sender().sendMessage(
				Messages.COMMAND_CHANNEL_LIST.append(Component.newline()).append(Component.join(JoinConfiguration.commas(true), channelComponents))
		);
	}

	private void executeMute(CommandContext<User> ctx) {
		User user = ctx.sender();
		Channel channel = ctx.get("channel");

		if (user.muteChannel(channel)) {
			user.sendMessage(Messages.COMMAND_CHANNEL_MUTED);
		} else {
			user.sendMessage(Messages.COMMAND_CHANNEL_ERROR_ALREADY_MUTED);
		}
	}

	private void executeUnmute(CommandContext<User> ctx) {
		User user = ctx.sender();
		Channel channel = ctx.get("channel");

		if (user.unMuteChannel(channel)) {
			user.sendMessage(Messages.COMMAND_CHANNEL_UNMUTED);
		} else {
			user.sendMessage(Messages.COMMAND_CHANNEL_ERROR_NOT_MUTED);
		}
	}
}
