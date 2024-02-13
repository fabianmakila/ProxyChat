package fi.fabianadrian.proxychat.common.service;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.config.ProxyChatConfig;
import fi.fabianadrian.proxychat.common.hook.FriendPluginHook;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.MessageSettings;
import fi.fabianadrian.proxychat.common.user.User;
import io.github.miniplaceholders.api.MiniPlaceholders;
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
	private final FriendPluginHook friendHook;
	private ProxyChatConfig.FormatSection formats;

	public MessageService(ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
		this.friendHook = proxyChat.platform().hookManager().friendHook().orElse(null);
		this.formats = proxyChat.configManager().mainConfig().formats();
	}

	public void reload() {
		this.formats = proxyChat.configManager().mainConfig().formats();
	}

	public void sendPrivateMessage(User sender, User receiver, String message) {
		if (!sender.hasPermission(BYPASS_PERMISSION)) {
			MessageSettings.PrivacySetting receiverPrivacySetting = receiver.messageSettings().privacySetting();

			if (receiver.hasBlockedUser(sender)) {
				return;
			}

			switch (receiverPrivacySetting) {
				case NOBODY -> {
					sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_DISALLOWED);
					return;
				}
				case FRIENDS -> {
					if (this.friendHook == null) {
						break;
					}

					if (!this.friendHook.areFriends(sender.uuid(), receiver.uuid())) {
						sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_DISALLOWED);
						return;
					}
				}
			}
		}

		// Message components
		Component senderComponent = messageSenderComponent(sender, receiver, message);
		Component receiverComponent = messageReceiverComponent(sender, receiver, message);
		Component spyComponent = messageSpyComponent(
				sender,
				receiver,
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
		TagResolver.Builder resolverBuilder = TagResolver.builder().resolvers(
				Placeholder.unparsed("sender", sender.name()),
				Placeholder.unparsed("message", message)
		);

		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			resolverBuilder = resolverBuilder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(sender));
		}

		Component component = this.miniMessage.deserialize(
				channel.format(),
				resolverBuilder.build()
		);

		for (User user : this.proxyChat.userManager().users()) {
			if (!user.hasPermission(channel.permission())) continue;
			user.sendMessage(component);
		}
	}

	public void sendWelcomeMessage(User user) {
		List<String> rawLines = this.proxyChat.configManager().mainConfig().welcomeMessage();

		if (rawLines.isEmpty()) return;

		TagResolver.Builder resolverBuilder = TagResolver.builder().resolvers(
				Placeholder.unparsed("name", user.name())
		);
		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			resolverBuilder = resolverBuilder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(user));
		}

		List<Component> lines = new ArrayList<>();
		for (String line : rawLines) {
			lines.add(this.miniMessage.deserialize(line, resolverBuilder.build()));
		}

		user.sendMessage(Component.join(JoinConfiguration.newlines(), lines));
	}

	private Component messageSenderComponent(User sender, User receiver, String message) {
		TagResolver.Builder resolverBuilder = TagResolver.builder().resolvers(
				Placeholder.component("sender", Messages.GENERAL_ME),
				Placeholder.unparsed("receiver", receiver.name()),
				Placeholder.unparsed("message", message)
		);

		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			resolverBuilder = resolverBuilder.resolver(MiniPlaceholders.getRelationalGlobalPlaceholders(sender, receiver));
		}

		return this.miniMessage.deserialize(this.formats.msg(), resolverBuilder.build());
	}

	private Component messageReceiverComponent(User sender, User receiver, String message) {
		TagResolver.Builder resolverBuilder = TagResolver.builder().resolvers(
				Placeholder.unparsed("sender", sender.name()),
				Placeholder.component("receiver", Messages.GENERAL_ME),
				Placeholder.unparsed("message", message)
		);

		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			resolverBuilder = resolverBuilder.resolver(MiniPlaceholders.getRelationalGlobalPlaceholders(sender, receiver));
		}

		return this.miniMessage.deserialize(this.formats.msg(), resolverBuilder.build());
	}

	private Component messageSpyComponent(User sender, User receiver, String message) {
		TagResolver.Builder resolverBuilder = TagResolver.builder().resolvers(
				Placeholder.unparsed("sender", sender.name()),
				Placeholder.unparsed("receiver", receiver.name()),
				Placeholder.unparsed("message", message)
		);

		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			resolverBuilder = resolverBuilder.resolver(MiniPlaceholders.getRelationalGlobalPlaceholders(sender, receiver));
		}

		return this.miniMessage.deserialize(this.formats.msgSpy(), resolverBuilder.build());
	}
}
