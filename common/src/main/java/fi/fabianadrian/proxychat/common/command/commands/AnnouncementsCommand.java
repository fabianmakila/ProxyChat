package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;

import java.util.Optional;

public final class AnnouncementsCommand extends ProxyChatCommand {

    public AnnouncementsCommand(ProxyChat proxyChat) {
        super(proxyChat, "announcements");
    }

    @Override
    public void register() {
        var builder = this.builder()
            .argument(BooleanArgument.optional("visible"))
            .senderType(User.class)
            .handler(this::executeAnnouncement);

        this.manager.command(builder);
    }

    private void executeAnnouncement(CommandContext<Commander> ctx) {
        Optional<Boolean> visibleOptional = ctx.getOptional("visible");
        User user = (User) ctx.getSender();
        boolean visible = visibleOptional.orElseGet(() -> !user.announcements());

        user.announcements(visible);

        ctx.getSender().sendMessage(visible ? Messages.COMMAND_ANNOUNCEMENTS_ON : Messages.COMMAND_ANNOUNCEMENTS_OFF);
    }
}
