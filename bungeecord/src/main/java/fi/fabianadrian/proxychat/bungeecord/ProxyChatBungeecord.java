package fi.fabianadrian.proxychat.bungeecord;

import fi.fabianadrian.proxychat.bungeecord.command.BungeecordConsoleCommander;
import fi.fabianadrian.proxychat.bungeecord.hook.BungeecordHookManager;
import fi.fabianadrian.proxychat.bungeecord.listener.LoginDisconnectListener;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.platform.Platform;
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

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public final class ProxyChatBungeecord extends Plugin implements Platform {
	private BungeeAudiences adventure;
	private BungeeCommandManager<Commander> commandManager;
	private ProxyChat proxyChat;
	private BungeecordHookManager hookManager;

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

		this.hookManager = new BungeecordHookManager(this);
		this.hookManager.initialize();
		this.proxyChat = new ProxyChat(this);

		registerListeners();

		if (this.proxyChat.configManager().mainConfig().metrics()) {
			new Metrics(this, 18435);
		}
	}

	@Override
	public void onDisable() {
		if (this.adventure != null) {
			this.adventure.close();
			this.adventure = null;
		}
		this.proxyChat.shutdown();
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
	public BungeeCommandManager<Commander> commandManager() {
		return this.commandManager;
	}

	@Override
	public HookManager hookManager() {
		return this.hookManager;
	}

	@Override
	public @NotNull Audience audience() {
		return this.adventure.all();
	}

	public ProxyChat proxyChat() {
		return this.proxyChat;
	}

	private void createCommandManager() {
		SenderMapper<CommandSender, Commander> senderMapper = SenderMapper.create(
				commandSource -> {
					if (commandSource instanceof ProxiedPlayer) {
						Optional<User> userOptional = this.proxyChat.userManager().user(((ProxiedPlayer) commandSource).getUniqueId());
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
