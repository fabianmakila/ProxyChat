package fi.fabianadrian.backend.common;

import fi.fabianadrian.backend.common.config.BackendConfigManager;
import fi.fabianadrian.backend.common.platform.BackendPlatform;

public final class ConversationBackend {
	private final BackendConfigManager configManager;

	public ConversationBackend(BackendPlatform platform) {
		this.configManager = new BackendConfigManager(platform.logger(), platform.dataDirectory());
		this.configManager.reload();
	}

	public BackendConfigManager configManager() {
		return this.configManager;
	}

	public void reload() {
		this.configManager.reload();
	}
}
