package fi.fabianadrian.proxychat.common.locale;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface Messages {
	Component COMMAND_CHANNEL_ERROR_ALREADY_MUTED = translatable(
			"proxychat.command.channel.already-muted",
			RED
	);
	Component COMMAND_CHANNEL_ERROR_NOT_MUTED = translatable(
			"proxychat.command.channel.not-muted",
			RED
	);
	Component COMMAND_CHANNEL_LIST = translatable(
			"proxychat.command.channel.list",
			GREEN
	);
	Component COMMAND_CHANNEL_MUTED = translatable(
			"proxychat.command.channel.muted",
			GREEN
	);
	Component COMMAND_CHANNEL_UNMUTED = translatable(
			"proxychat.command.channel.unmuted",
			GREEN
	);
	Component COMMAND_MESSAGE_ERROR_DISALLOWED = translatable(
			"proxychat.command.message.disallowed",
			RED
	);
	Component COMMAND_MESSAGESETTINGS_ANNOUNCEMENTS_HIDDEN = translatable(
			"proxychat.command.messagesettings.announcements.hidden",
			RED
	);
	Component COMMAND_MESSAGESETTINGS_ANNOUNCEMENTS_VISIBLE = translatable(
			"proxychat.command.messagesettings.announcements.visible",
			GREEN
	);
	Component COMMAND_MESSAGESETTINGS_GLOBALCHAT_HIDDEN = translatable(
			"proxychat.command.messagesettings.globalchat.hidden",
			RED
	);
	Component COMMAND_MESSAGESETTINGS_GLOBALCHAT_VISIBLE = translatable(
			"proxychat.command.messagesettings.globalchat.visible",
			GREEN
	);
	Component COMMAND_MESSAGESETTINGS_ALLOW_NOBODY = translatable(
			"proxychat.command.messagesettings.privacy.nobody",
			GREEN
	);
	Component COMMAND_MESSAGESETTINGS_ALLOW_FRIENDS = translatable(
			"proxychat.command.messagesettings.privacy.friends",
			GREEN
	);
	Component COMMAND_MESSAGESETTINGS_ALLOW_EVERYONE = translatable(
			"proxychat.command.messagesettings.privacy.everyone",
			GREEN
	);
	Component COMMAND_MESSAGESETTINGS_SPY_DISABLE = translatable(
			"proxychat.command.messagesettings.spy.disable",
			RED
	);
	Component COMMAND_MESSAGESETTINGS_SPY_ENABLE = translatable(
			"proxychat.command.messagesettings.spy.enable",
			GREEN
	);
	Component COMMAND_PROXYCHAT_RELOAD_SUCCESS = translatable(
			"proxychat.command.proxychat.reload.success",
			Color.PRIMARY.textColor
	);
	Component COMMAND_REPLY_ERROR_NO_LAST_MESSAGED = translatable(
			"proxychat.command.reply.no-last-messaged",
			RED
	);
	Component COMMAND_REPLY_ERROR_LAST_MESSAGED_OFFLINE = translatable(
			"proxychat.command.reply.offline",
			RED
	);

	Component GENERAL_ME = translatable("proxychat.general.me");

	Component GENERAL_UNKNOWN = translatable("proxychat.general.unknown");
}
