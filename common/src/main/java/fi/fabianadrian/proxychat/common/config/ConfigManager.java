package fi.fabianadrian.proxychat.common.config;

import fi.fabianadrian.proxychat.common.ProxyChat;
import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public final class ConfigManager {
    private final Logger logger;
    private final ConfigurationHelper<ProxyChatConfig> mainConfigHelper;
    private final ConfigurationHelper<AnnouncementsConfig> announcementsConfigHelper;
    private final ConfigurationHelper<ChannelsConfig> channelsConfigHelper;

    private volatile ProxyChatConfig mainConfigData;
    private volatile AnnouncementsConfig announcementsConfigData;
    private volatile ChannelsConfig channelsConfigData;

    public ConfigManager(ProxyChat proxyChat) {
        this.logger = proxyChat.platform().logger();

        SnakeYamlOptions yamlOptions = new SnakeYamlOptions.Builder()
            .yamlSupplier(() -> {
                DumperOptions dumperOptions = new DumperOptions();
                // Enables comments
                dumperOptions.setProcessComments(true);
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                return new Yaml(dumperOptions);
            })
            .commentMode(CommentMode.fullComments())
            .build();

        Path dataDirectory = proxyChat.platform().dataDirectory();

        this.mainConfigHelper = new ConfigurationHelper<>(
            dataDirectory,
            "config.yml",
            SnakeYamlConfigurationFactory.create(
                ProxyChatConfig.class,
                ConfigurationOptions.defaults(),
                yamlOptions
            )
        );

        this.announcementsConfigHelper = new ConfigurationHelper<>(
            dataDirectory,
            "announcements.yml",
            SnakeYamlConfigurationFactory.create(
                AnnouncementsConfig.class,
                ConfigurationOptions.defaults(),
                yamlOptions
            )
        );

        this.channelsConfigHelper = new ConfigurationHelper<>(
            dataDirectory,
            "channels.yml",
            SnakeYamlConfigurationFactory.create(
                ChannelsConfig.class,
                ConfigurationOptions.defaults(),
                yamlOptions
            )
        );
    }

    public void reload() {
        try {
            this.mainConfigData = this.mainConfigHelper.reloadConfigData();
            this.announcementsConfigData = this.announcementsConfigHelper.reloadConfigData();
            this.channelsConfigData = this.channelsConfigHelper.reloadConfigData();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);

        } catch (ConfigFormatSyntaxException ex) {
            this.mainConfigData = this.mainConfigHelper.getFactory().loadDefaults();
            this.announcementsConfigData = this.announcementsConfigHelper.getFactory().loadDefaults();
            this.channelsConfigData = this.channelsConfigHelper.getFactory().loadDefaults();
            this.logger.error(
                "The yaml syntax in your configuration is invalid. " +
                    "Check your YAML syntax with a tool such as https://yaml-online-parser.appspot.com/",
                ex
            );
        } catch (InvalidConfigException ex) {
            this.mainConfigData = this.mainConfigHelper.getFactory().loadDefaults();
            this.announcementsConfigData = this.announcementsConfigHelper.getFactory().loadDefaults();
            this.channelsConfigData = this.channelsConfigHelper.getFactory().loadDefaults();
            this.logger.error(
                "One of the values in your configuration is not valid. " +
                    "Check to make sure you have specified the right data types.",
                ex
            );
        }
    }

    public ProxyChatConfig mainConfig() {
        ProxyChatConfig configData = this.mainConfigData;
        if (configData == null) {
            throw new IllegalStateException("Configuration has not been loaded yet");
        }
        return configData;
    }

    public AnnouncementsConfig announcementsConfig() {
        AnnouncementsConfig configData = this.announcementsConfigData;
        if (configData == null) {
            throw new IllegalStateException("Configuration has not been loaded yet");
        }
        return configData;
    }

    public ChannelsConfig channelsConfig() {
        ChannelsConfig configData = this.channelsConfigData;
        if (configData == null) {
            throw new IllegalStateException("Configuration has not been loaded yet");
        }
        return configData;
    }
}
