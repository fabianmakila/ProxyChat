package fi.fabianadrian.proxychat.common.channel;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.config.ConfigLoader;
import fi.fabianadrian.proxychat.common.user.User;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class ChannelRegistry {

    private final ProxyChat proxyChat;
    private final Path configChannelDir;
    private final Map<String, Channel> registry = new HashMap<>();

    public ChannelRegistry(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.configChannelDir = proxyChat.platform().dataDirectory().resolve("channels");

        saveExampleConfigChannel();
        loadConfigChannels();
    }

    public Channel channel(String name) {
        return this.registry.get(name);
    }

    public Collection<Channel> channels() {
        return Collections.unmodifiableCollection(registry.values());
    }

    public void register(Channel channel) throws ChannelRegisterException {
        String name = channel.name();

        this.registerChannelCommands(channel);

        this.registry.put(name, channel);
    }

    private void loadConfigChannels() {
        if (!Files.exists(this.configChannelDir)) return;
        try (final Stream<Path> paths = Files.walk(this.configChannelDir)) {
            paths.forEach(path -> {
                final String fileName = path.getFileName().toString();

                if (!fileName.endsWith(".conf") || fileName.equals("example.conf")) {
                    return;
                }

                ConfigLoader<Channel> loader = this.loader(path);
                try {
                    Channel channel = loader.load();
                    loader.save(channel);
                    this.register(channel);
                } catch (ConfigurateException | ChannelRegistry.ChannelRegisterException e) {
                    throw new IllegalStateException("Failed to load channel", e);
                }
            });
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void saveExampleConfigChannel() {
        ConfigLoader<Channel> loader = new ConfigLoader<>(
                Channel.class,
                this.configChannelDir.resolve("example.conf"),
                options -> options.header("ProxyChat example channel configuration.")
        );
        try {
            loader.save(new Channel());
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to save example channel config", e);
        }

    }

    private ConfigLoader<Channel> loader(Path path) {
        return new ConfigLoader<>(
                Channel.class,
                path,
                UnaryOperator.identity()
        );
    }

    //TODO Maybe move most of this under command package???
    private void registerChannelCommands(Channel channel) {
        if (channel.commandName().isBlank()) return;

        CommandManager<Commander> commandManager = this.proxyChat.platform().commandManager();
        var builder = commandManager.commandBuilder(
                        channel.commandName(),
                        channel.commandAliases(),
                        commandManager.createDefaultCommandMeta()
                )
                .permission(channel.permission())
                .argument(StringArgument.of("message", StringArgument.StringMode.GREEDY))
                .senderType(User.class)
                .handler(handler -> {
                    final String message = handler.get("message");
                    this.proxyChat.messageService().sendChannelMessage(channel, handler.getSender(), message);
                });

        commandManager.command(builder);
    }

    public static class ChannelRegisterException extends Exception {
        private static final long serialVersionUID = 1814071943131367347L;

        public ChannelRegisterException(String errorMessage) {
            super(errorMessage);
        }
    }
}
