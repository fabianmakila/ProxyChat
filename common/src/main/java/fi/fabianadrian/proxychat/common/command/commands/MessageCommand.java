package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.velocity.arguments.PlayerArgument;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.CommandPermissions;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.user.User;

public final class MessageCommand extends ProxyChatCommand {
    public MessageCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("message", "msg", "dm")
            .permission(CommandPermissions.MESSAGE.permission())
            .senderType(User.class)
            .argument(PlayerArgument.of("receiver"))
            .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
            .handler(this::executeMessage);

        this.commandManager.command(builder);
    }

    private void executeMessage(CommandContext<Commander> ctx) {
        this.proxyChat.messageService().sendPrivateMessage((User) ctx.getSender(), ctx.get("receiver"), ctx.get("message"));
    }
}
