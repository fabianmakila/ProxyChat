package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;

public final class AnnouncementsCommand extends AbstractCommand {

    public AnnouncementsCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("announcements").permission(CommandPermissions.BROADCAST);

        this.commandManager.command(builder.literal("hide").senderType(Player.class).handler(this::executeHide));
        this.commandManager.command(builder.literal("show").senderType(Player.class).handler(this::executeShow));
    }

    private void executeShow(CommandContext<CommandSource> ctx) {
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());
        user.announcements(true);
        ctx.getSender().sendMessage(Messages.COMMAND_ANNOUNCEMENTS_ON);
    }

    private void executeHide(CommandContext<CommandSource> ctx) {
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());
        user.announcements(false);
        ctx.getSender().sendMessage(Messages.COMMAND_ANNOUNCEMENTS_OFF);
    }
}
