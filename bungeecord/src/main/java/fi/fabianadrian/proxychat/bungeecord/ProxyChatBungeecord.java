package fi.fabianadrian.proxychat.bungeecord;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bungee.BungeeCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.bstats.bungeecord.Metrics;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class ProxyChatBungeecord extends Plugin implements Platform {
    private BungeeAudiences adventure;
    private CommandManager<Commander> commandManager;
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

        this.commandManager = new BungeeCommandManager<>(
            this,
            CommandExecutionCoordinator.simpleCoordinator(),
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

        this.hookManager = new BungeecordHookManager(this);
        this.proxyChat = new ProxyChat(this);
        registerListeners();

        // bStats
        new Metrics(this, 18435);
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
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
    public CommandManager<Commander> commandManager() {
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

    private void registerListeners() {
        PluginManager manager = this.getProxy().getPluginManager();
        Stream.of(
            new LoginDisconnectListener(this)
        ).forEach(listener -> manager.registerListener(this, listener));
    }

    public ProxyChat proxyChat() {
        return this.proxyChat;
    }
}
