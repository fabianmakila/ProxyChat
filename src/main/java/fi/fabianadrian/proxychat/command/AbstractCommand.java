package fi.fabianadrian.proxychat.command;

import cloud.commandframework.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import fi.fabianadrian.proxychat.ProxyChat;

public abstract class AbstractCommand {
    protected final ProxyChat proxyChat;
    protected final CommandManager<CommandSource> commandManager;

    public AbstractCommand(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.commandManager = proxyChat.commandManager();
    }

    public abstract void register();
}
