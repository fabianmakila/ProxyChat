package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.CommandPermission;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;

import java.util.Optional;
import java.util.UUID;

public final class ReplyCommand extends ProxyChatCommand {
    public ReplyCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("reply", "r")
            .permission(CommandPermission.MESSAGE.permission())
            .senderType(User.class)
            .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
            .handler(this::executeReply);

        this.commandManager.command(builder);
    }

    private void executeReply(CommandContext<Commander> ctx) {
        User user = (User) ctx.getSender();

        UUID lastMessaged = user.lastMessaged();
        if (lastMessaged == null) {
            ctx.getSender().sendMessage(Messages.COMMAND_REPLY_ERROR_NO_LAST_MESSAGED);
            return;
        }

        Optional<User> receiver = this.proxyChat.userManager().user(lastMessaged);
        if (receiver.isEmpty()) {
            ctx.getSender().sendMessage(Messages.COMMAND_REPLY_ERROR_LAST_MESSAGED_OFFLINE);
            return;
        }

        this.proxyChat.messageService().sendPrivateMessage((User) ctx.getSender(), receiver.get(), ctx.get("message"));
    }
}
