package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.locale.Messages;

public final class ProxyChatCommand extends fi.fabianadrian.proxychat.common.command.ProxyChatCommand {
    public ProxyChatCommand(ProxyChat proxyChat) {
        super(proxyChat, "proxychat");
    }

    @Override
    public void register() {
        var builder = this.builder()
            .literal("reload")
            .handler(this::executeReload);

        this.manager.command(builder);
    }

    private void executeReload(CommandContext<Commander> ctx) {
        this.proxyChat.reload();
        ctx.getSender().sendMessage(Messages.COMMAND_PROXYCHAT_RELOAD_SUCCESS);
    }
}
