package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.format.FormatComponentProvider;

public final class BroadcastCommand extends ProxyChatCommand {
    private final FormatComponentProvider componentProvider;

    public BroadcastCommand(ProxyChat proxyChat) {
        super(proxyChat, "broadcast", "bc");
        this.componentProvider = proxyChat.formatComponentProvider();
    }

    @Override
    public void register() {
        var builder = this.builder()
            .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
            .handler(this::executeBroadcast);

        this.manager.command(builder);
    }

    private void executeBroadcast(CommandContext<Commander> ctx) {
        String message = ctx.get("message");
        this.proxyChat.platform().sendMessage(
            this.componentProvider.broadcastComponent(message)
        );
    }
}
