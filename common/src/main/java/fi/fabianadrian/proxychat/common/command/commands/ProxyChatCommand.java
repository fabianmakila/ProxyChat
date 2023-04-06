package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.CommandPermissions;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.locale.Messages;

public final class ProxyChatCommand extends fi.fabianadrian.proxychat.common.command.ProxyChatCommand {
    public ProxyChatCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("proxychat").permission(CommandPermissions.PROXYCHAT.permission());

        this.commandManager.command(builder
            .literal("reload")
            .handler(this::executeReload)
        );
    }

    private void executeReload(CommandContext<Commander> ctx) {
        this.proxyChat.reload();
        ctx.getSender().sendMessage(Messages.COMMAND_PROXYCHAT_RELOAD_SUCCESS);
    }
}
