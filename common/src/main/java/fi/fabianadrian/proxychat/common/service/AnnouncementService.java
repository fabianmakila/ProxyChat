package fi.fabianadrian.proxychat.common.service;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.config.AnnouncementsConfig;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.*;

public final class AnnouncementService {

    private final ProxyChat proxyChat;
    private AnnouncementsConfig config;
    private int index = 0;
    private ScheduledFuture<?> scheduledTask;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public AnnouncementService(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.config = proxyChat.configManager().announcementsConfig();
    }

    public void reload() {
        this.config = this.proxyChat.configManager().announcementsConfig();

        // No need to run the task if there are no announcements
        if (config.announcements().isEmpty()) {
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
            ThreadLocalRandom.current().nextInt(this.config.announcements().size()) :
            (this.index >= this.config.announcements().size() - 1) ? 0 : this.index + 1;

        Component announcement = this.miniMessage.deserialize(this.config.prefix() + this.config.announcements().get(this.index));
        for (User user : this.proxyChat.userManager().users()) {
            if (user.announcements()) {
                user.sendMessage(announcement);
            }
        }
    }
}
