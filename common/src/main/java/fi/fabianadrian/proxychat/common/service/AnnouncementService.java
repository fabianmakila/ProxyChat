package fi.fabianadrian.proxychat.common.service;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.config.ProxyChatConfig;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.concurrent.*;

public final class AnnouncementService {

	private final ProxyChat proxyChat;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private ProxyChatConfig.AnnouncementsSection config;
	private int index = 0;
	private ScheduledFuture<?> scheduledTask;

	public AnnouncementService(ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
		this.config = proxyChat.configManager().mainConfig().announcements();
	}

	public void reload() {
		this.config = this.proxyChat.configManager().mainConfig().announcements();

		// No need to run the task if there are no announcements
		if (config.messages().isEmpty()) {
			if (this.scheduledTask != null) {
				this.scheduledTask.cancel(false);
			}
			return;
		}

		// Only create the task if it does not already exist
		if (this.scheduledTask == null) {
			this.scheduledTask = this.scheduler.scheduleAtFixedRate(this::sendAnnouncement, 0, config.interval(), TimeUnit.MINUTES);
		}
	}

	public void sendAnnouncement() {
		this.index = this.config.random() ?
				ThreadLocalRandom.current().nextInt(this.config.messages().size()) :
				(this.index >= this.config.messages().size() - 1) ? 0 : this.index + 1;

		Component announcement = this.miniMessage.deserialize(
				this.config.format(),
				Placeholder.parsed("message", this.config.messages().get(this.index))
		);

		for (User user : this.proxyChat.userManager().users()) {
			if (user.messageSettings().announcements()) {
				user.sendMessage(announcement);
			}
		}
	}
}
