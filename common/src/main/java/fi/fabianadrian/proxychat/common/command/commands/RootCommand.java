package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;

public final class RootCommand extends ProxyChatCommand {
    public RootCommand(ProxyChat proxyChat) {
        super(proxyChat, "proxychat");
    }

    @Override
    public void register() {
        this.manager.command(
            subCommand("reload").handler(this::executeReload)
        );

        var debugBuilder = subCommand("debug");
        this.manager.command(
            debugBuilder.literal("announcement")
                .handler(this::executeDebugAnnouncement)
        );
    }

    private void executeReload(CommandContext<Commander> ctx) {
        this.proxyChat.reload();
        ctx.getSender().sendMessage(Messages.COMMAND_PROXYCHAT_RELOAD_SUCCESS);
    }

    private void executeDebugAnnouncement(CommandContext<Commander> ctx) {
        this.proxyChat.announcementService().sendAnnouncement();
    }
}
