package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.CommandPermissions;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;

import java.util.Optional;

public final class AnnouncementsCommand extends ProxyChatCommand {

    public AnnouncementsCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("announcements").permission(CommandPermissions.ANNOUNCEMENTS.permission());
        this.commandManager.command(builder.argument(BooleanArgument.optional("visible")).senderType(User.class).handler(this::executeBroadcast));
    }

    private void executeBroadcast(CommandContext<Commander> ctx) {
        Optional<Boolean> visibleOptional = ctx.getOptional("visible");
        User user = (User) ctx.getSender();
        boolean visible = visibleOptional.orElseGet(() -> !user.announcements());

        user.announcements(visible);

        ctx.getSender().sendMessage(visible ? Messages.COMMAND_ANNOUNCEMENTS_ON : Messages.COMMAND_ANNOUNCEMENTS_OFF);
    }
}
