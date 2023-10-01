package fi.fabianadrian.proxychat.common.service;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.config.ProxyChatConfig;
import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;

public final class MessageService {

	private static final String BYPASS_PERMISSION = "proxychat.command.message.bypass";
	private final ProxyChat proxyChat;
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final FriendHook friendHook;
	private ProxyChatConfig.FormatSection formats;

	public MessageService(ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
		this.friendHook = proxyChat.platform().hookManager().friendHook();
		this.formats = proxyChat.configManager().mainConfig().formats();
	}

	public void reload() {
		this.formats = proxyChat.configManager().mainConfig().formats();
	}

	public void sendPrivateMessage(User sender, User receiver, String message) {
		if (sender == receiver) {
			sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_SELF);
			return;
		}

		if (!sender.hasPermission(BYPASS_PERMISSION)) {
			switch (receiver.messageSettings().privacySetting()) {
				case NOBODY:
					sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_DISALLOWED);
					return;
				case FRIENDS:
					if (!this.friendHook.areFriends(sender.uuid(), receiver.uuid())) {
						sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_DISALLOWED);
						return;
					}
			}
		}

		// Message components
		Component senderComponent = messageSenderComponent(receiver.name(), message);
		Component receiverComponent = messageReceiverComponent(sender.name(), message);
		Component spyComponent = messageSpyComponent(
				sender.name(),
				receiver.name(),
				message
		);

		// Send components
		sender.sendMessage(senderComponent);
		receiver.sendMessage(receiverComponent);

		receiver.lastMessaged(sender.uuid());

		//Sends message spy component to every online user that has spy true
		for (User user : this.proxyChat.userManager().users()) {
			if (user == sender || user == receiver || !user.messageSettings().spy()) continue;
			user.sendMessage(spyComponent);
		}
	}

	public void sendChannelMessage(Channel channel, User sender, String message) {
		Component component = miniMessage.deserialize(
				channel.format(),
				TagResolver.resolver(
						Placeholder.unparsed("sender", sender.name()),
						Placeholder.unparsed("message", message)
				)
		);

		for (User user : this.proxyChat.userManager().users()) {
			if (!user.hasPermission(channel.permission())) continue;
			user.sendMessage(component);
		}
	}

	public void sendWelcomeMessage(User user) {
		List<String> rawLines = this.proxyChat.configManager().mainConfig().welcomeMessage();

		if (rawLines.isEmpty()) return;

		List<Component> lines = new ArrayList<>();
		rawLines.forEach(line -> lines.add(miniMessage.deserialize(line, TagResolver.resolver(
				Placeholder.unparsed("name", user.name())
		))));

		user.sendMessage(Component.join(JoinConfiguration.newlines(), lines));
	}

	private Component messageSenderComponent(String receiverName, String message) {
		return this.miniMessage.deserialize(
				this.formats.msg(),
				TagResolver.resolver(
						Placeholder.component("sender", Messages.GENERAL_ME),
						Placeholder.unparsed("receiver", receiverName),
						Placeholder.unparsed("message", message)
				)
		);
	}

	private Component messageReceiverComponent(String senderName, String message) {
		return this.miniMessage.deserialize(
				this.formats.msg(),
				TagResolver.resolver(
						Placeholder.unparsed("sender", senderName),
						Placeholder.component("receiver", Messages.GENERAL_ME),
						Placeholder.unparsed("message", message)
				)
		);
	}

	private Component messageSpyComponent(String senderName, String receiverName, String message) {
		return this.miniMessage.deserialize(
				this.formats.msgSpy(),
				TagResolver.resolver(
						Placeholder.unparsed("sender", senderName),
						Placeholder.unparsed("receiver", receiverName),
						Placeholder.unparsed("message", message)
				)
		);
	}
}
