package fi.fabianadrian.proxychat.common;

import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.commands.*;
import fi.fabianadrian.proxychat.common.command.processor.ProxyChatCommandPreprocessor;
import fi.fabianadrian.proxychat.common.command.processor.ProxyChatCommandSuggestionProcessor;
import fi.fabianadrian.proxychat.common.config.ConfigManager;
import fi.fabianadrian.proxychat.common.format.FormatComponentProvider;
import fi.fabianadrian.proxychat.common.locale.TranslationManager;
import fi.fabianadrian.proxychat.common.platform.Platform;
import fi.fabianadrian.proxychat.common.service.AnnouncementService;
import fi.fabianadrian.proxychat.common.service.MessageService;
import fi.fabianadrian.proxychat.common.user.UserManager;

import java.util.stream.Stream;

public final class ProxyChat {
    private final Platform platform;
    private final ConfigManager configManager;
    private final FormatComponentProvider formatComponentProvider;
    private final UserManager userManager;
    private final TranslationManager translationManager;
    private final MessageService messageService;
    private final ChannelRegistry channelRegistry;
    private final AnnouncementService announcementService;

    public ProxyChat(Platform platform) {
        this.platform = platform;

        this.configManager = new ConfigManager(this);
        this.configManager.reload();

        this.userManager = new UserManager(this);

        this.translationManager = new TranslationManager(this);
        this.translationManager.reload();

        this.formatComponentProvider = new FormatComponentProvider(this);

        this.messageService = new MessageService(this);

        this.platform.commandManager().commandSuggestionProcessor(new ProxyChatCommandSuggestionProcessor<>());
        this.platform.commandManager().registerCommandPreProcessor(new ProxyChatCommandPreprocessor<>(this));

        registerCommands();

        // Channel registry registers commands for channels so this must be after command manager is defined.
        this.channelRegistry = new ChannelRegistry(this);

        this.announcementService = new AnnouncementService(this);
        this.announcementService.reload();
    }

    public Platform platform() {
        return platform;
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

    public FormatComponentProvider formatComponentProvider() {
        return formatComponentProvider;
    }

    public void reload() {
        this.configManager.reload();

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
}
