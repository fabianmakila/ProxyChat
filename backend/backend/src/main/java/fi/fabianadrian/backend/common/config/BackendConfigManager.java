package fi.fabianadrian.backend.common.config;

import fi.fabianadrian.conversation.common.config.ConfigManager;
import org.slf4j.Logger;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.helper.ConfigurationHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class BackendConfigManager extends ConfigManager {
	private final ConfigurationHelper<BackendConfig> helper;
	private BackendConfig data;

	public BackendConfigManager(Logger logger, Path configDirectory) {
		super(logger);

		this.helper = new ConfigurationHelper<>(
				configDirectory,
				"config.yml",
				SnakeYamlConfigurationFactory.create(
						BackendConfig.class,
						ConfigurationOptions.defaults(),
						defaultYamlOptionsBuilder().build()
				)
		);
	}

	@Override
	public void reload() {
		try {
			this.data = this.helper.reloadConfigData();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		} catch (ConfigFormatSyntaxException ex) {
			this.data = this.helper.getFactory().loadDefaults();
			this.logger.error(
					"The yaml syntax in your configuration is invalid. " +
							"Check your YAML syntax with a tool such as https://yaml-online-parser.appspot.com/",
					ex
			);
		} catch (InvalidConfigException ex) {
			this.data = this.helper.getFactory().loadDefaults();
			this.logger.error(
					"One of the values in your configuration is not valid. " +
							"Check to make sure you have specified the right data types.",
					ex
			);
		}
	}

	public BackendConfig config() {
		if (this.data == null) {
			throw new IllegalStateException("Configuration has not been loaded yet");
		}
		return this.data;
	}
}
