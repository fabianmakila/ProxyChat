package fi.fabianadrian.proxychat.channel;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.api.channel.Channel;
import fi.fabianadrian.proxychat.config.ConfigLoader;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class ChannelRegistry {

    private final ProxyChat proxyChat;
    private final Path configChannelDir;
    private final Map<String, Channel> registry = new HashMap<>();

    public ChannelRegistry(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.configChannelDir = proxyChat.dataDirectory().resolve("channels");

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
        //TODO Is the validation needed here?
        if (!Channel.isValidName(name)) {
            throw new ChannelRegisterException("Invalid channel name: " + channel.name());
        }

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

                ConfigLoader<ConfigChannel> loader = this.loader(path);
                try {
                    ConfigChannel channel = loader.load();
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
        ConfigLoader<ConfigChannel> loader = new ConfigLoader<>(
                ConfigChannel.class,
                this.configChannelDir.resolve("example.conf"),
                options -> options.header("ProxyChat example channel configuration.")
        );
        try {
            loader.save(new ConfigChannel());
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to save example channel config", e);
        }

    }

    private ConfigLoader<ConfigChannel> loader(Path path) {
        return new ConfigLoader<>(
                ConfigChannel.class,
                path,
                UnaryOperator.identity()
        );
    }

    //TODO Maybe move most of this under command package???
    private void registerChannelCommands(Channel channel) {
        if (channel.commandName().isBlank()) return;

        CommandManager<CommandSource> commandManager = this.proxyChat.commandManager();
        var builder = commandManager.commandBuilder(
                        channel.commandName(),
                        channel.commandAliases(),
                        commandManager.createDefaultCommandMeta()
                )
                .permission(channel.permission())
                .argument(StringArgument.optional("message", StringArgument.StringMode.GREEDY))
                .senderType(Player.class)
                .handler(handler -> {
                    Player player = (Player) handler.getSender();
                    if (handler.contains("message")) {
                        final String message = handler.get("message");
                        this.proxyChat.messageService().sendChannelMessage(channel, player, message);
                    } else {
                        User user = this.proxyChat.userManager().user(player.getUniqueId());
                        if (channel.name().equals(user.selectedChannel())) {
                            user.clearSelectedChannel();
                            player.sendMessage(Messages.COMMAND_CHANNEL_DESELECT);
                            return;
                        }

                        user.selectedChannel(channel);
                        player.sendMessage(Messages.COMMAND_CHANNEL_SELECT);
                    }
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
