package fi.fabianadrian.conversation.backend.paper.dependency;

import fi.fabianadrian.backend.common.dependency.BackendDependencyManager;

public final class PaperDependencyManager extends BackendDependencyManager {
	private boolean miniPlaceholders = false;

	@Override
	public boolean isMiniPlaceholdersPresent() {
		return this.miniPlaceholders;
	}

	public void markMiniPlaceholdersPresent() {
		this.miniPlaceholders = true;
	}
}
