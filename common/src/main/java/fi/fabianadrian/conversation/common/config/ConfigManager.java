package fi.fabianadrian.conversation.common.config;

import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;

public abstract class ConfigManager {
	protected final Logger logger;

	public ConfigManager(Logger logger) {
		this.logger = logger;
	}

	public abstract void reload();

	protected SnakeYamlOptions.Builder defaultYamlOptionsBuilder() {
		return new SnakeYamlOptions.Builder()
				.yamlSupplier(() -> {
					DumperOptions dumperOptions = new DumperOptions();
					// Enables comments
					dumperOptions.setProcessComments(true);
					dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
					return new Yaml(dumperOptions);
				})
				.commentMode(CommentMode.fullComments());
	}
}
