package fi.fabianadrian.backend.common.platform;

import fi.fabianadrian.backend.common.dependency.BackendDependencyManager;
import fi.fabianadrian.conversation.common.platform.Platform;

public interface BackendPlatform extends Platform {
	BackendDependencyManager dependencyManager();
}
