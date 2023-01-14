package fi.fabianadrian.proxychat.common.service;

import com.velocitypowered.api.scheduler.ScheduledTask;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.config.AnnouncementsConfig;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class AnnouncementService {

    private final ProxyChat proxyChat;
    private AnnouncementsConfig config;
    private ScheduledTask announcerTask;
    private int index = 0;
    private final Random random = new Random();

    public AnnouncementService(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.config = proxyChat.configManager().announcementsConfig();
    }

    public void reload() {
        this.config = this.proxyChat.configManager().announcementsConfig();

        if (this.announcerTask != null) {
            this.announcerTask.cancel();
        }

        this.announcerTask = this.proxyChat.proxyServer().getScheduler().buildTask(this.proxyChat, () -> {
            if (this.config.announcements().isEmpty()) {
                return;
            }

            sendAnnouncement();
        }).repeat(config.interval(), TimeUnit.MINUTES).schedule();
    }

    private void sendAnnouncement() {
        if (this.config.random()) {
            this.index = this.random.nextInt(this.config.announcements().size() - 1);
        } else {
            if (index >= this.config.announcements().size() - 1) {
                index = 0;
            } else {
                index += 1;
            }
        }

        Component announcement = MiniMessage.miniMessage().deserialize(this.config.prefix() + this.config.announcements().get(this.index));
        for (User user : this.proxyChat.userManager().users()) {
            if (!user.announcements()) continue;
            user.player().sendMessage(announcement);
        }
    }
}
