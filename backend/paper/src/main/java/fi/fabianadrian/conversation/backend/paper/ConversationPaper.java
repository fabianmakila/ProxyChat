package fi.fabianadrian.conversation.backend.paper;


import fi.fabianadrian.backend.common.platform.BackendPlatform;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class ConversationPaper extends JavaPlugin implements BackendPlatform {
	@Override
	public void onEnable() {


	}

	private void registerListeners() {

	}

	@Override
	public Logger logger() {
		return null;
	}

	@Override
	public Path dataDirectory() {
		return null;
	}
}
