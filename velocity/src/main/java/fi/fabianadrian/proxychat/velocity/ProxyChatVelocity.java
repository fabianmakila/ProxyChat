package fi.fabianadrian.proxychat.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.velocity.listener.ChatListener;
import fi.fabianadrian.proxychat.velocity.listener.LoginDisconnectListener;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.stream.Stream;

@Plugin(
        id = "proxychat",
        name = "ProxyChat",
        version = "0.1.0",
        url = "https://github.com/fabianmakila/ProxyChat",
        description = "A simple chat plugin for Velocity.",
        authors = {"FabianAdrian"}
)
public final class ProxyChatVelocity extends ProxyChat {
    private final Path dataDirectory;
    private final Metrics.Factory metricsFactory;
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public ProxyChatVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerListeners();

        // bStats
        metricsFactory.make(this, 15557);
    }

    private void registerListeners() {
        EventManager manager = this.server.getEventManager();
        Stream.of(
                new ChatListener(this),
                new LoginDisconnectListener(this)
        ).forEach(listener -> manager.register(this, listener));
    }

    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public Path dataDirectory() {
        return this.dataDirectory;
    }
}
