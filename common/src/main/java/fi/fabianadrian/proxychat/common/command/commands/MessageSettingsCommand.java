package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.MessageSettings;
import fi.fabianadrian.proxychat.common.user.User;

import java.util.Optional;

public class MessageSettingsCommand extends ProxyChatCommand {

	public MessageSettingsCommand(ProxyChat proxyChat) {
		super(proxyChat, "messagesettings", "msgs");
	}

	@Override
	public void register() {
		this.manager.command(subCommand("privacy")
				.senderType(User.class)
				.argument(EnumArgument.of(MessageSettings.PrivacySetting.class, "privacySetting"))
				.handler(this::executeAllow)
		);
		this.manager.command(subCommand("spy")
				.senderType(User.class)
				.argument(BooleanArgument.optional("enabled"))
				.handler(this::executeSpy)
		);
	}

	private void executeSpy(CommandContext<Commander> ctx) {
		Optional<Boolean> enabledOptional = ctx.getOptional("enabled");
		User user = (User) ctx.getSender();
		boolean value = enabledOptional.orElseGet(() -> !user.messageSettings().spy());

		user.messageSettings().spy(value);
		ctx.getSender().sendMessage(value ? Messages.COMMAND_MESSAGES_SPY_ENABLE : Messages.COMMAND_MESSAGES_SPY_DISABLE);
	}

	private void executeAllow(CommandContext<Commander> ctx) {
		User user = (User) ctx.getSender();
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
