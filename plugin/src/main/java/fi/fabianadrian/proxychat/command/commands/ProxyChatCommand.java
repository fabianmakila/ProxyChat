package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.locale.Messages;

public final class ProxyChatCommand extends AbstractCommand {
    public ProxyChatCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("proxychat")
                .permission(CommandPermissions.PROXYCHAT.permission())
                .literal("reload")
                .handler(this::executeReload);

        this.commandManager.command(builder);
    }

    private void executeReload(CommandContext<CommandSource> ctx) {
        this.proxyChat.reload();
        ctx.getSender().sendMessage(Messages.COMMAND_PROXYCHAT_RELOAD_SUCCESS);
    }
}
