package fi.fabianadrian.proxychat.common.service;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.config.ProxyChatConfig;
import fi.fabianadrian.proxychat.common.hook.FriendPluginHook;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.MessageSettings;
import fi.fabianadrian.proxychat.common.user.User;
import fi.fabianadrian.proxychat.common.user.UserManager;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.kyori.adventure.text.Component.translatable;

public final class MessageService {
	private static final Component COMPONENT_MESSAGE_ERROR_DISALLOWED = translatable(
			"proxychat.command.message.disallowed",
			NamedTextColor.RED
	);
	private static final String PERMISSION_BYPASS = "proxychat.command.message.bypass";
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
		if (!isAllowedToMessage(sender, receiver)) {
			sender.sendMessage(COMPONENT_MESSAGE_ERROR_DISALLOWED);
			return;
		}

		sender.sendMessage(messageSenderComponent(sender, receiver, message));
		receiver.sendMessage(messageReceiverComponent(sender, receiver, message));
		receiver.lastMessaged(sender.uuid());

		//Sends message spy component to every online user that has spy true
		Component spyComponent = messageSpyComponent(sender, receiver, message);
		for (User user : this.proxyChat.userManager().users()) {
			if (user == sender || user == receiver || !user.messageSettings().spy()) continue;
			user.sendMessage(spyComponent);
		}
	}

	public void sendChannelMessage(Channel channel, User sender, String message) {
		Component component = this.miniMessage.deserialize(
				channel.format(),
				TagResolver.resolver(
						senderResolver(sender),
						messageResolver(message),
						miniPlaceholdersAudienceResolver(sender)
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

	public void sendGlobalMessage(UUID senderUUID, String message, List<UUID> recipientUUIDList) {
		UserManager userManager = this.proxyChat.userManager();

		Optional<User> senderOptional = userManager.user(senderUUID);
		if (senderOptional.isEmpty()) {
			return;
		}

		List<User> recipients = new ArrayList<>();
		recipientUUIDList.forEach(uuid -> {
			Optional<User> userOptional = userManager.user(uuid);
			if (userOptional.isEmpty()) {
				return;
			}
			User user = userOptional.get();

			if (!user.messageSettings().globalChat()) {
				return;
			}

			recipients.add(user);
		});

		Component messageComponent = this.miniMessage.deserialize(
				this.formats.global(),
				TagResolver.resolver(
						senderResolver(senderOptional.get()),
						messageResolver(message),
						miniPlaceholdersAudienceResolver(senderOptional.get())
				)
		);
		recipients.forEach(recipient -> recipient.sendMessage(messageComponent));
	}

	private boolean isAllowedToMessage(User sender, User receiver) {
		if (sender.hasPermission(PERMISSION_BYPASS)) {
			return true;
		}

		MessageSettings.PrivacySetting receiverPrivacySetting = receiver.messageSettings().privacySetting();

		if (receiver.hasBlockedUser(sender)) {
			return false;
		}

		switch (receiverPrivacySetting) {
			case NOBODY -> {
				return false;
			}
			case FRIENDS -> {
				if (this.friendHook == null) {
					return true;
				}

				return this.friendHook.areFriends(sender.uuid(), receiver.uuid());
			}
		}

		return true;
	}

	private Component messageSenderComponent(User sender, User receiver, String message) {
		TagResolver resolver = TagResolver.resolver(
				senderResolverMe(sender),
				receiverResolver(receiver),
				messageResolver(message),
				miniPlaceholdersRelationalResolver(sender, receiver)
		);

		return this.miniMessage.deserialize(this.formats.msg(), resolver);
	}

	private Component messageReceiverComponent(User sender, User receiver, String message) {
		TagResolver resolver = TagResolver.resolver(
				senderResolver(sender),
				receiverResolverMe(receiver),
				messageResolver(message),
				miniPlaceholdersRelationalResolver(sender, receiver)
		);

		return this.miniMessage.deserialize(this.formats.msg(), resolver);
	}

	private Component messageSpyComponent(User sender, User receiver, String message) {
		TagResolver resolver = TagResolver.resolver(
				senderResolver(sender),
				receiverResolver(receiver),
				messageResolver(message),
				miniPlaceholdersRelationalResolver(sender, receiver)
		);

		return this.miniMessage.deserialize(this.formats.msgSpy(), resolver);
	}

	private TagResolver senderResolver(User sender) {
		return TagResolver.resolver(
				Placeholder.unparsed("sender_name", sender.name()),
				Placeholder.component("sender_server", sender.currentServerName())
		);
	}

	private TagResolver senderResolverMe(User sender) {
		return TagResolver.resolver(
				Placeholder.component("sender_name", Messages.GENERAL_ME),
				Placeholder.component("sender_server", sender.currentServerName())
		);
	}

	private TagResolver receiverResolver(User receiver) {
		return TagResolver.resolver(
				Placeholder.unparsed("receiver_name", receiver.name()),
				Placeholder.component("receiver_server", receiver.currentServerName())
		);
	}

	private TagResolver receiverResolverMe(User receiver) {
		return TagResolver.resolver(
				Placeholder.component("receiver_name", Messages.GENERAL_ME),
				Placeholder.component("receiver_server", receiver.currentServerName())
		);
	}

	private TagResolver messageResolver(String message) {
		return Placeholder.unparsed("message", message);
	}

	private TagResolver miniPlaceholdersAudienceResolver(User sender) {
		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			return MiniPlaceholders.getAudienceGlobalPlaceholders(sender);
		}
		return TagResolver.empty();
	}

	private TagResolver miniPlaceholdersRelationalResolver(User sender, User receiver) {
		if (this.proxyChat.platform().hookManager().isMiniplaceholdersAvailable()) {
			return MiniPlaceholders.getRelationalGlobalPlaceholders(sender, receiver);
		}
		return TagResolver.empty();
	}
}
