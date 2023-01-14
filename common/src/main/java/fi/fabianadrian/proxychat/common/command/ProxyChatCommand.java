package fi.fabianadrian.proxychat.common.command;

import cloud.commandframework.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import fi.fabianadrian.proxychat.common.ProxyChat;

public abstract class ProxyChatCommand {
    protected final ProxyChat proxyChat;
    protected final CommandManager<CommandSource> commandManager;

    public ProxyChatCommand(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.commandManager = proxyChat.commandManager();
    }

    public abstract void register();
}
