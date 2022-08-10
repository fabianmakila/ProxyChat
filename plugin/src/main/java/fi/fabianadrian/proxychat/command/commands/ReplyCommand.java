package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;

import java.util.Optional;
import java.util.UUID;

public final class ReplyCommand extends AbstractCommand {
    public ReplyCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("reply", "r")
                .permission(CommandPermissions.MESSAGE.permission())
                .senderType(Player.class)
                .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
                .handler(this::executeReply);

        this.commandManager.command(builder);
    }

    private void executeReply(CommandContext<CommandSource> ctx) {
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());

        UUID lastMessaged = user.lastMessaged();
        if (lastMessaged == null) {
            ctx.getSender().sendMessage(Messages.COMMAND_REPLY_ERROR_NO_LAST_MESSAGED);
            return;
        }

        Optional<Player> receiver = this.proxyChat.proxyServer().getPlayer(lastMessaged);
        if (receiver.isEmpty()) {
            ctx.getSender().sendMessage(Messages.COMMAND_REPLY_ERROR_LAST_MESSAGED_OFFLINE);
            return;
        }

        this.proxyChat.messageService().sendPrivateMessage((Player) ctx.getSender(), receiver.get(), ctx.get("message"));
    }
}
