package fi.fabianadrian.conversation.backend.paper;

import fi.fabianadrian.backend.common.ConversationBackend;
import fi.fabianadrian.backend.common.platform.BackendPlatform;
import fi.fabianadrian.conversation.backend.paper.dependency.PaperDependencyManager;
import fi.fabianadrian.conversation.backend.paper.listener.ChatListener;
import fi.fabianadrian.conversation.backend.paper.listener.ServerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;

public final class ConversationPaper extends JavaPlugin implements BackendPlatform {
	private final PaperDependencyManager dependencyManager = new PaperDependencyManager();
	private ConversationBackend conversation;

	@Override
	public void onEnable() {
		this.conversation = new ConversationBackend(this);

		registerListeners();
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new ChatListener(this),
				new ServerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}

	@Override
	public Logger logger() {
		return getSLF4JLogger();
	}

	@Override
	public Path dataDirectory() {
		return getDataFolder().toPath();
	}

	@Override
	public PaperDependencyManager dependencyManager() {
		return this.dependencyManager;
	}

	public ConversationBackend conversation() {
		return this.conversation;
	}
}
