package fi.fabianadrian.proxychat.common.command;

import cloud.commandframework.CommandManager;
import fi.fabianadrian.proxychat.common.ProxyChat;

public abstract class ProxyChatCommand {
    protected final ProxyChat proxyChat;
    protected final CommandManager<Commander> commandManager;

    public ProxyChatCommand(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.commandManager = proxyChat.platform().commandManager();
    }

    public abstract void register();
}
