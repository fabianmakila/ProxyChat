package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.user.MessageSettings;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.BooleanParser;
import org.incendo.cloud.parser.standard.EnumParser;

import java.util.Optional;

import static net.kyori.adventure.text.Component.translatable;

public final class MessageSettingsCommand extends ProxyChatCommand {
	private static final Component COMPONENT_ANNOUNCEMENTS_HIDDEN = translatable(
			"proxychat.command.messagesettings.announcements.hidden",
			NamedTextColor.RED
	);
	private static final Component COMPONENT_ANNOUNCEMENTS_VISIBLE = translatable(
			"proxychat.command.messagesettings.announcements.visible",
			NamedTextColor.GREEN
	);
	private static final Component COMPONENT_GLOBALCHAT_HIDDEN = translatable(
			"proxychat.command.messagesettings.globalchat.hidden",
			NamedTextColor.RED
	);
	private static final Component COMPONENT_GLOBALCHAT_VISIBLE = translatable(
			"proxychat.command.messagesettings.globalchat.visible",
			NamedTextColor.GREEN
	);
	private static final Component COMPONENT_PRIVACY_NOBODY = translatable(
			"proxychat.command.messagesettings.privacy.nobody",
			NamedTextColor.GREEN
	);
	private static final Component COMPONENT_PRIVACY_FRIENDS = translatable(
			"proxychat.command.messagesettings.privacy.friends",
			NamedTextColor.GREEN
	);
	private static final Component COMPONENT_PRIVACY_EVERYONE = translatable(
			"proxychat.command.messagesettings.privacy.everyone",
			NamedTextColor.GREEN
	);
	private static final Component COMPONENT_SPY_DISABLE = translatable(
			"proxychat.command.messagesettings.spy.disable",
			NamedTextColor.RED
	);
	private static final Component COMPONENT_SPY_ENABLE = translatable(
			"proxychat.command.messagesettings.spy.enable",
			NamedTextColor.GREEN
	);

	public MessageSettingsCommand(ProxyChat proxyChat) {
		super(proxyChat, "messagesettings", "msgs");
	}

	@Override
	public void register() {
		this.manager.command(subCommand("announcements")
				.senderType(User.class)
				.optional("visible", BooleanParser.booleanParser())
				.handler(this::executeAnnouncement)
		);
		this.manager.command(subCommand("globalchat")
				.senderType(User.class)
				.optional("visible", BooleanParser.booleanParser())
				.handler(this::executeGlobalChat)
		);
		this.manager.command(subCommand("privacy")
				.senderType(User.class)
				.required("privacySetting", EnumParser.enumParser(MessageSettings.PrivacySetting.class))
				.handler(this::executeAllow)
		);
		this.manager.command(subCommand("spy")
				.senderType(User.class)
				.required("enabled", BooleanParser.booleanParser())
				.handler(this::executeSpy)
		);
	}

	private void executeSpy(CommandContext<User> ctx) {
		Optional<Boolean> enabledOptional = ctx.optional("enabled");
		User user = ctx.sender();
		boolean value = enabledOptional.orElseGet(() -> !user.messageSettings().spy());

		user.messageSettings().spy(value);
		ctx.sender().sendMessage(value ? COMPONENT_SPY_ENABLE : COMPONENT_SPY_DISABLE);
	}

	private void executeAllow(CommandContext<User> ctx) {
		User user = ctx.sender();
		MessageSettings.PrivacySetting privacySetting = ctx.get("privacySetting");

		user.messageSettings().privacySetting(privacySetting);

		switch (privacySetting) {
			case NOBODY:
				user.sendMessage(COMPONENT_PRIVACY_NOBODY);
				break;
			case FRIENDS:
				user.sendMessage(COMPONENT_PRIVACY_FRIENDS);
				break;
			case EVERYONE:
				user.sendMessage(COMPONENT_PRIVACY_EVERYONE);
				break;
		}
	}

	private void executeAnnouncement(CommandContext<User> ctx) {
		Optional<Boolean> visibleOptional = ctx.optional("visible");
		User user = ctx.sender();

		boolean visible = visibleOptional.orElseGet(() -> !user.messageSettings().announcements());

		user.messageSettings().announcements(visible);
		user.sendMessage(visible ? COMPONENT_ANNOUNCEMENTS_VISIBLE : COMPONENT_ANNOUNCEMENTS_HIDDEN);
	}

	private void executeGlobalChat(CommandContext<User> ctx) {
		Optional<Boolean> visibleOptional = ctx.optional("visible");
		User user = ctx.sender();

		boolean visible = visibleOptional.orElseGet(() -> !user.messageSettings().globalChat());

		user.messageSettings().globalChat(visible);
		user.sendMessage(visible ? COMPONENT_GLOBALCHAT_VISIBLE : COMPONENT_GLOBALCHAT_HIDDEN);
	}
}
