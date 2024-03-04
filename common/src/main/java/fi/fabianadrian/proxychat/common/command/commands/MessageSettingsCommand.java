package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.MessageSettings;
import fi.fabianadrian.proxychat.common.user.User;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.BooleanParser;
import org.incendo.cloud.parser.standard.EnumParser;

import java.util.Optional;

public class MessageSettingsCommand extends ProxyChatCommand {

	public MessageSettingsCommand(ProxyChat proxyChat) {
		super(proxyChat, "messagesettings", "msgs");
	}

	@Override
	public void register() {
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
		ctx.sender().sendMessage(value ? Messages.COMMAND_MESSAGES_SPY_ENABLE : Messages.COMMAND_MESSAGES_SPY_DISABLE);
	}

	private void executeAllow(CommandContext<User> ctx) {
		User user = ctx.sender();
		MessageSettings.PrivacySetting privacySetting = ctx.get("privacySetting");

		user.messageSettings().privacySetting(privacySetting);

		switch (privacySetting) {
			case NOBODY:
				user.sendMessage(Messages.COMMAND_MESSAGES_ALLOW_NOBODY);
				break;
			case FRIENDS:
				user.sendMessage(Messages.COMMAND_MESSAGES_ALLOW_FRIENDS);
				break;
			case EVERYONE:
				user.sendMessage(Messages.COMMAND_MESSAGES_ALLOW_EVERYONE);
				break;
		}
	}
}
