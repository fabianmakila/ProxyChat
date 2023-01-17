package fi.fabianadrian.proxychat.bungeecord;

import cloud.commandframework.CommandManager;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.platform.Platform;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import org.slf4j.Logger;

import java.nio.file.Path;

public class ProxyChatBungeecord extends Plugin implements Platform {
    private BungeeAudiences adventure;

    public BungeeAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Cannot retrieve audience provider while plugin is not enabled");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        this.adventure = BungeeAudiences.create(this);
        new ProxyChat(this);
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    @Override
    public Logger logger() {
        return null;
    }

    @Override
    public Path dataDirectory() {
        return null;
    }

    @Override
    public CommandManager<Commander> commandManager() {
        return null;
    }
}
