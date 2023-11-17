package fi.fabianadrian.proxychat.velocity;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.platform.Platform;
import fi.fabianadrian.proxychat.common.user.User;
import fi.fabianadrian.proxychat.velocity.command.VelocityConsoleCommander;
import fi.fabianadrian.proxychat.velocity.hook.VelocityHookManager;
import fi.fabianadrian.proxychat.velocity.listener.LoginDisconnectListener;
import net.kyori.adventure.audience.Audience;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Plugin(
		id = "proxychat",
		name = "ProxyChat",
		version = "1.0.0-beta.4",
		url = "https://github.com/fabianmakila/ProxyChat",
		description = "A simple chat plugin for Minecraft proxies.",
		authors = {"FabianAdrian"},
		dependencies = {
				@Dependency(id = "partyandfriends", optional = true),
				@Dependency(id = "premiumvanish", optional = true)
		}
)
public final class ProxyChatVelocity implements Platform {
	private final Path dataDirectory;
	private final Metrics.Factory metricsFactory;
	private final ProxyServer server;
	private final Logger logger;
	private ProxyChat proxyChat;
	private CommandManager<Commander> commandManager;
	private VelocityHookManager hookManager;

	@Inject
	public ProxyChatVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
		this.server = server;
		this.logger = logger;
		this.dataDirectory = dataDirectory;
		this.metricsFactory = metricsFactory;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		this.commandManager = new VelocityCommandManager<>(
				this.server.getPluginManager().ensurePluginContainer(this),
				this.server,
				CommandExecutionCoordinator.simpleCoordinator(),
				commandSource -> {
					if (commandSource instanceof Player) {
						Optional<User> userOptional = this.proxyChat.userManager().user(((Player) commandSource).getUniqueId());
						if (userOptional.isPresent()) {
							return userOptional.get();
						}
						throw new IllegalStateException("User was not loaded");
					}
					return new VelocityConsoleCommander(commandSource);
				},
				commander -> {
					if (commander instanceof VelocityConsoleCommander) {
						return ((VelocityConsoleCommander) commander).commandSource();
					}

					Optional<Player> playerOptional = this.server.getPlayer(((User) commander).uuid());
					if (playerOptional.isPresent()) {
						return playerOptional.get();
					}
					throw new IllegalArgumentException();
				}
		);

		this.hookManager = new VelocityHookManager(this);
		this.proxyChat = new ProxyChat(this);
		registerListeners();

		// bStats
		this.metricsFactory.make(this, 15557);
	}

	@Subscribe
	public void onProxyShutdown(ProxyShutdownEvent event) {
		this.proxyChat.shutdown();
	}

	private void registerListeners() {
		EventManager manager = this.server.getEventManager();
		Stream.of(
				new LoginDisconnectListener(this.proxyChat)
		).forEach(listener -> manager.register(this, listener));
	}

	@Override
	public Logger logger() {
		return this.logger;
	}

	@Override
	public Path dataDirectory() {
		return this.dataDirectory;
	}

	@Override
	public CommandManager<Commander> commandManager() {
		return this.commandManager;
	}

	@Override
	public HookManager hookManager() {
		return this.hookManager;
	}

	@Override
	public @NotNull Audience audience() {
		return this.server;
	}

	public ProxyServer server() {
		return this.server;
	}
}
