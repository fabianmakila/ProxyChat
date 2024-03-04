package fi.fabianadrian.proxychat.common;

import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.command.commands.*;
import fi.fabianadrian.proxychat.common.command.processor.ProxyChatCommandPreprocessor;
import fi.fabianadrian.proxychat.common.config.ConfigManager;
import fi.fabianadrian.proxychat.common.locale.TranslationManager;
import fi.fabianadrian.proxychat.common.platform.Platform;
import fi.fabianadrian.proxychat.common.platform.PlatformPlayer;
import fi.fabianadrian.proxychat.common.service.AnnouncementService;
import fi.fabianadrian.proxychat.common.service.MessageService;
import fi.fabianadrian.proxychat.common.service.NameService;
import fi.fabianadrian.proxychat.common.user.User;
import fi.fabianadrian.proxychat.common.user.UserManager;
import net.kyori.adventure.identity.Identity;
import org.incendo.cloud.translations.LocaleExtractor;
import org.incendo.cloud.translations.TranslationBundle;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

public final class ProxyChat {
	private final Platform platform;
	private final ConfigManager configManager;
	private final UserManager userManager;
	private final TranslationManager translationManager;
	private final MessageService messageService;
	private final ChannelRegistry channelRegistry;
	private final AnnouncementService announcementService;
	private final NameService nameService;

	public ProxyChat(Platform platform) {
		this.platform = platform;

		this.configManager = new ConfigManager(this);
		this.configManager.reload();

		this.userManager = new UserManager(this);
		this.userManager.reload();

		this.translationManager = new TranslationManager(this);
		this.translationManager.reload();

		this.messageService = new MessageService(this);
		this.nameService = new NameService(this);

		this.platform.commandManager().registerCommandPreProcessor(new ProxyChatCommandPreprocessor<>(this));

		LocaleExtractor<Commander> extractor = commander -> commander.getOrDefault(Identity.LOCALE, Locale.ENGLISH);
		TranslationBundle<Commander> bundle = TranslationBundle.core(extractor);
		this.platform.commandManager().captionRegistry().registerProvider(bundle);

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
				new BlockCommand(this),
				new BlockListCommand(this),
				new BroadcastCommand(this),
				new ChannelCommand(this),
				new MessageCommand(this),
				new MessageSettingsCommand(this),
				new ReplyCommand(this),
				new RootCommand(this),
				new UnblockCommand(this)
		).forEach(ProxyChatCommand::register);
	}

	public ConfigManager configManager() {
		return configManager;
	}

	public void reload() {
		this.configManager.reload();
		this.translationManager.reload();
		this.userManager.reload();
		this.announcementService.reload();
		this.messageService.reload();
		this.nameService.reload();
	}

	public void handleLogin(PlatformPlayer player) {
		User user = this.userManager.loadUser(player);

		this.messageService.sendWelcomeMessage(user);
		this.nameService.update(user);
	}

	public void handleDisconnect(UUID uuid) {
		this.userManager.unloadUser(uuid);
	}

	public void shutdown() {
		this.nameService.save();
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

	public AnnouncementService announcementService() {
		return this.announcementService;
	}

	public NameService nameService() {
		return this.nameService;
	}
}
