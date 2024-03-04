package fi.fabianadrian.proxychat.common.command.commands;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.BooleanParser;

import java.util.Optional;

public final class AnnouncementsCommand extends ProxyChatCommand {

	public AnnouncementsCommand(ProxyChat proxyChat) {
		super(proxyChat, "announcements");
	}

	@Override
	public void register() {
		var builder = this.builder()
				.optional("visible", BooleanParser.booleanParser())
				.senderType(User.class)
				.handler(this::executeAnnouncement);

		this.manager.command(builder);
	}

	private void executeAnnouncement(CommandContext<User> ctx) {
		Optional<Boolean> visibleOptional = ctx.optional("visible");
		User user = ctx.sender();

		boolean visible = visibleOptional.orElseGet(() -> !user.announcements());

		user.announcements(visible);
		user.sendMessage(visible ? Messages.COMMAND_ANNOUNCEMENTS_ON : Messages.COMMAND_ANNOUNCEMENTS_OFF);
	}
}
