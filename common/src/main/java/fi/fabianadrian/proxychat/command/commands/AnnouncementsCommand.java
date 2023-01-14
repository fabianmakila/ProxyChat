package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;

import java.util.Optional;

public final class AnnouncementsCommand extends ProxyChatCommand {

    public AnnouncementsCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("announcements").permission(CommandPermissions.ANNOUNCEMENTS.permission());
        this.commandManager.command(builder.argument(BooleanArgument.optional("visible")).senderType(Player.class).handler(this::executeBroadcast));
    }

    private void executeBroadcast(CommandContext<CommandSource> ctx) {
        Optional<Boolean> visibleOptional = ctx.getOptional("visible");
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());
        boolean visible = visibleOptional.orElseGet(() -> !user.announcements());

        user.announcements(visible);

        ctx.getSender().sendMessage(visible ? Messages.COMMAND_ANNOUNCEMENTS_ON : Messages.COMMAND_ANNOUNCEMENTS_OFF);
    }
}
