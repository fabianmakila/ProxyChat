package fi.fabianadrian.proxychat.common;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.commands.*;
import fi.fabianadrian.proxychat.common.command.processor.ProxyChatCommandPreprocessor;
import fi.fabianadrian.proxychat.common.command.processor.ProxyChatCommandSuggestionProcessor;
import fi.fabianadrian.proxychat.common.config.ConfigManager;
import fi.fabianadrian.proxychat.common.format.FormatComponentProvider;
import fi.fabianadrian.proxychat.common.listener.ChatListener;
import fi.fabianadrian.proxychat.common.listener.LoginDisconnectListener;
import fi.fabianadrian.proxychat.common.locale.TranslationManager;
import fi.fabianadrian.proxychat.common.service.AnnouncementService;
import fi.fabianadrian.proxychat.common.service.MessageService;
import fi.fabianadrian.proxychat.common.user.UserManager;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

@Plugin(
        id = "proxychat",
        name = "ProxyChat",
        version = "0.1.0",
        url = "https://github.com/fabianmakila/ProxyChat",
        description = "A simple chat plugin for Velocity.",
        authors = {"FabianAdrian"}
)
public final class ProxyChat {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final Metrics.Factory metricsFactory;
    private VelocityCommandManager<CommandSource> commandManager;
    private ConfigManager configManager;
    private FormatComponentProvider formatComponentProvider;
    private UserManager userManager;
    private TranslationManager translationManager;
    private MessageService messageService;
    private ChannelRegistry channelRegistry;
    private AnnouncementService announcementService;

    @Inject
    public ProxyChat(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.configManager = new ConfigManager(this);
        this.configManager.loadConfigs();

        this.userManager = new UserManager(this);

        this.translationManager = new TranslationManager(this);
        this.translationManager.reload();

        this.formatComponentProvider = new FormatComponentProvider(this);

        this.messageService = new MessageService(this);

        this.commandManager = new VelocityCommandManager<>(this.server.getPluginManager().ensurePluginContainer(this), this.server, CommandExecutionCoordinator.simpleCoordinator(), Function.identity(), Function.identity());
        this.commandManager.commandSuggestionProcessor(new ProxyChatCommandSuggestionProcessor<>());
        this.commandManager.registerCommandPreProcessor(new ProxyChatCommandPreprocessor<>(this));

        registerCommands();

        // Channel registry registers commands for channels so this must be after command manager is defined.
        this.channelRegistry = new ChannelRegistry(this);

        this.announcementService = new AnnouncementService(this);
        this.announcementService.reload();

        registerListeners();

        // bStats
        metricsFactory.make(this, 15557);
    }

    public Logger logger() {
        return this.logger;
    }

    public Path dataDirectory() {
        return this.dataDirectory;
    }

    public CommandManager<CommandSource> commandManager() {
        return this.commandManager;
    }

    private void registerCommands() {
        Stream.of(
                new AnnouncementsCommand(this),
                new BroadcastCommand(this),
                new ChannelCommand(this),
                new MessageCommand(this),
                new MessagesCommand(this),
                new fi.fabianadrian.proxychat.common.command.commands.ProxyChatCommand(this),
                new ReplyCommand(this)
        ).forEach(ProxyChatCommand::register);
    }

    public ConfigManager configManager() {
        return configManager;
    }

    public ProxyServer proxyServer() {
        return server;
    }

    public FormatComponentProvider formatComponentProvider() {
        return formatComponentProvider;
    }

    public void reload() {
        this.configManager.loadConfigs();

        this.announcementService.reload();
        this.translationManager.reload();
    }

    public UserManager userManager() {
        return userManager;
    }

    public MessageService messageService() {
        return messageService;
    }

    public ChannelRegistry channelRegistry() {
        return this.channelRegistry;
    }

    private void registerListeners() {
        EventManager manager = this.server.getEventManager();
        Stream.of(
                new ChatListener(this),
                new LoginDisconnectListener(this)
        ).forEach(listener -> manager.register(this, listener));
    }
}
