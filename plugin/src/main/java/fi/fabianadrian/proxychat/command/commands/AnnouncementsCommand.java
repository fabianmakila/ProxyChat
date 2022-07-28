package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;

import java.util.Optional;

public final class AnnouncementsCommand extends AbstractCommand {

    public AnnouncementsCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("announcements").permission(CommandPermissions.BROADCAST);
        this.commandManager.command(builder.argument(BooleanArgument.optional("visible")).senderType(Player.class).handler(this::executeBroadcast));
    }

    private void executeBroadcast(CommandContext<CommandSource> ctx) {
        Optional<Boolean> visibleOptional = ctx.getOptional("visible");
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());
        boolean visible;
        if (visibleOptional.isEmpty()) {
            visible = !user.announcements();
        } else {
            visible = visibleOptional.get();
        }

        user.announcements(visible);

        if (visible) {
            ctx.getSender().sendMessage(Messages.COMMAND_ANNOUNCEMENTS_ON);
        } else {
            ctx.getSender().sendMessage(Messages.COMMAND_ANNOUNCEMENTS_OFF);
        }
    }
}
