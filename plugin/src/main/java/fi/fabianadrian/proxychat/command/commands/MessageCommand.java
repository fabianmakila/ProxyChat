package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.velocity.arguments.PlayerArgument;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;

public final class MessageCommand extends AbstractCommand {
    public MessageCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("message", "msg", "dm")
                .permission(CommandPermissions.MESSAGE)
                .senderType(Player.class)
                .argument(PlayerArgument.of("receiver"))
                .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
                .handler(this::executeMessage);

        this.commandManager.command(builder);
    }

    private void executeMessage(CommandContext<CommandSource> ctx) {
        this.proxyChat.messageService().sendPrivateMessage((Player) ctx.getSender(), ctx.get("receiver"), ctx.get("message"));
    }
}
