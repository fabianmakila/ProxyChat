package fi.fabianadrian.proxychat.common.config;

import fi.fabianadrian.proxychat.common.ProxyChat;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;

public final class ConfigManager {

    private final ConfigLoader<ProxyChatConfig> mainConfigLoader;
    private final ConfigLoader<AnnouncementsConfig> announcementsConfigLoader;

    private ProxyChatConfig mainConfig;
    private AnnouncementsConfig announcementsConfig;

    public ConfigManager(ProxyChat proxyChat) {
        Path dataDirectory = proxyChat.dataDirectory();
        this.mainConfigLoader = new ConfigLoader<>(
                ProxyChatConfig.class,
                dataDirectory.resolve("main.conf"),
                options -> options.header("ProxyChat Main Configuration")
        );

        this.announcementsConfigLoader = new ConfigLoader<>(
                AnnouncementsConfig.class,
                dataDirectory.resolve("announcements.conf"),
                options -> options.header("Announcements Configuration")
        );
    }

    public void loadConfigs() {
        try {
            this.mainConfig = this.mainConfigLoader.load();
            this.mainConfigLoader.save(this.mainConfig);

            this.announcementsConfig = this.announcementsConfigLoader.load();
            this.announcementsConfigLoader.save(this.announcementsConfig);
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to load config", e);
        }
    }

    public ProxyChatConfig mainConfig() {
        if (this.mainConfig == null) {
            throw new IllegalStateException("Config has not yet been loaded");
        }
        return this.mainConfig;
    }

    public AnnouncementsConfig announcementsConfig() {
        if (this.announcementsConfig == null) {
            throw new IllegalStateException("Config has not yet been loaded");
        }
        return this.announcementsConfig;
    }
}
