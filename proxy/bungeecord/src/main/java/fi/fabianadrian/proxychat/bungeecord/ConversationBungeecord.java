package fi.fabianadrian.proxychat.bungeecord;

import fi.fabianadrian.proxychat.bungeecord.command.BungeecordConsoleCommander;
import fi.fabianadrian.proxychat.bungeecord.hook.BungeecordDependencyManager;
import fi.fabianadrian.proxychat.bungeecord.listener.LoginDisconnectListener;
import fi.fabianadrian.proxychat.common.ConversationProxy;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.platform.ProxyPlatform;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.bstats.bungeecord.Metrics;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.bungee.BungeeCommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public final class ConversationBungeecord extends Plugin implements ProxyPlatform {
	private BungeeAudiences adventure;
	private BungeeCommandManager<Commander> commandManager;
	private ConversationProxy conversation;
	private BungeecordDependencyManager dependencyManager;

	public BungeeAudiences adventure() {
		if (this.adventure == null) {
			throw new IllegalStateException("Cannot retrieve audience provider while plugin is not enabled");
		}
		return this.adventure;
	}

	@Override
	public void onEnable() {
		this.adventure = BungeeAudiences.create(this);

		createCommandManager();

		this.dependencyManager = new BungeecordDependencyManager(this);
		this.dependencyManager.initialize();
		this.conversation = new ConversationProxy(this);

		registerListeners();

		if (this.conversation.configManager().mainConfig().metrics()) {
			new Metrics(this, 18435);
		}
	}

	@Override
	public void onDisable() {
		if (this.adventure != null) {
			this.adventure.close();
			this.adventure = null;
		}
		this.conversation.shutdown();
	}

	@Override
	public Logger logger() {
		return LoggerFactory.getLogger(this.getDescription().getName());
	}

	@Override
	public Path dataDirectory() {
		return getDataFolder().toPath();
	}

	@Override
	public BungeeCommandManager<Commander> commandManager() {
		return this.commandManager;
	}

	@Override
	public BungeecordDependencyManager dependencyManager() {
		return this.dependencyManager;
	}

	@Override
	public @NotNull Audience audience() {
		return this.adventure.all();
	}

	public ConversationProxy conversation() {
		return this.conversation;
	}

	private void createCommandManager() {
		SenderMapper<CommandSender, Commander> senderMapper = SenderMapper.create(
				commandSource -> {
					if (commandSource instanceof ProxiedPlayer) {
						Optional<User> userOptional = this.conversation.userManager().user(((ProxiedPlayer) commandSource).getUniqueId());
						if (userOptional.isPresent()) {
							return userOptional.get();
						}
						throw new IllegalStateException("User was not loaded");
					}
					return new BungeecordConsoleCommander(commandSource, this.adventure.sender(commandSource));
				},
				commander -> {
					if (commander instanceof BungeecordConsoleCommander) {
						return ((BungeecordConsoleCommander) commander).commandSender();
					}

					ProxiedPlayer player = this.getProxy().getPlayer(((User) commander).uuid());
					if (player == null) {
						throw new IllegalArgumentException();
					}

					return player;
				}
		);

		this.commandManager = new BungeeCommandManager<>(
				this,
				ExecutionCoordinator.simpleCoordinator(),
				senderMapper
		);
	}

	private void registerListeners() {
		PluginManager manager = this.getProxy().getPluginManager();
		Stream.of(
				new LoginDisconnectListener(this)
		).forEach(listener -> manager.registerListener(this, listener));
	}
}
