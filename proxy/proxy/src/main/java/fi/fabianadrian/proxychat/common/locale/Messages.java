package fi.fabianadrian.proxychat.common.locale;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface Messages {
	Component COMMAND_ANNOUNCEMENTS_OFF = translatable(
			"proxychat.command.announcements.off",
			RED
	);
	Component COMMAND_ANNOUNCEMENTS_ON = translatable(
			"proxychat.command.announcements.on",
			GREEN
	);
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
	Component COMMAND_MESSAGES_ALLOW_NOBODY = translatable(
			"proxychat.command.messages.privacy.nobody",
			GREEN
	);
	Component COMMAND_MESSAGES_ALLOW_FRIENDS = translatable(
			"proxychat.command.messages.privacy.friends",
			GREEN
	);
	Component COMMAND_MESSAGES_ALLOW_EVERYONE = translatable(
			"proxychat.command.messages.privacy.everyone",
			GREEN
	);
	Component COMMAND_MESSAGES_SPY_DISABLE = translatable(
			"proxychat.command.messages.spy.disable",
			RED
	);
	Component COMMAND_MESSAGES_SPY_ENABLE = translatable(
			"proxychat.command.messages.spy.enable",
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
}
