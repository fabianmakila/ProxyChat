package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.format.FormatComponentProvider;

public final class BroadcastCommand extends AbstractCommand {
    private final FormatComponentProvider componentProvider;

    public BroadcastCommand(ProxyChat proxyChat) {
        super(proxyChat);
        this.componentProvider = proxyChat.formatComponentProvider();
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("broadcast", "bc")
                .permission(CommandPermissions.BROADCAST)
                .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
                .handler(this::executeBroadcast);

        this.commandManager.command(builder);
    }

    private void executeBroadcast(CommandContext<CommandSource> ctx) {
        String message = ctx.get("message");
        this.proxyChat.proxyServer().sendMessage(
                this.componentProvider.broadcastComponent(message)
        );
    }
}
