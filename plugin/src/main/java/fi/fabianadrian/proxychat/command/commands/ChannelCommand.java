package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.api.channel.Channel;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.command.argument.ChannelArgument;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ChannelCommand extends AbstractCommand {
    public ChannelCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("channel", "ch")
                .permission(CommandPermissions.CHANNEL);

        this.commandManager.command(builder.literal("list")
                .handler(this::executeList)
        );

        this.commandManager.command(builder.literal("mute")
                .argument(ChannelArgument.of("channel"))
                .senderType(Player.class)
                .handler(this::executeMute)
        );

        this.commandManager.command(builder.literal("unmute")
                .argument(ChannelArgument.of("channel"))
                .senderType(Player.class)
                .handler(this::executeUnmute)
        );
    }

    //TODO Maybe move some of this logic to elsewhere? Seems kinda messy
    private void executeList(CommandContext<CommandSource> ctx) {
        Stream<Channel> channels = this.proxyChat.channelRegistry().channels().stream().filter(channel -> ctx.getSender().hasPermission(channel.permission()));
        List<Component> channelComponents = new ArrayList<>();

        if ((ctx.getSender() instanceof Player)) {
            User user = this.proxyChat.userManager().user((Player) ctx.getSender());
            channels.forEach(channel -> channelComponents.add(user.mutedChannels().contains(channel.name()) ? Component.text(channel.name(), NamedTextColor.DARK_GRAY) : Component.text(channel.name(), NamedTextColor.GREEN)));
        } else {
            channels.forEach(channel -> channelComponents.add(Component.text(channel.name(), NamedTextColor.GREEN)));
        }

        ctx.getSender().sendMessage(
                Messages.COMMAND_CHANNEL_LIST.append(Component.newline()).append(Component.join(JoinConfiguration.commas(true), channelComponents))
        );
    }

    private void executeMute(CommandContext<CommandSource> ctx) {
        Player player = (Player) ctx.getSender();
        User user = this.proxyChat.userManager().user(player);
        Channel channel = ctx.get("channel");

        if (user.muteChannel(channel)) {
            player.sendMessage(Messages.COMMAND_CHANNEL_MUTED);
        } else {
            player.sendMessage(Messages.COMMAND_CHANNEL_ERROR_ALREADY_MUTED);
        }
    }

    private void executeUnmute(CommandContext<CommandSource> ctx) {
        Player player = (Player) ctx.getSender();
        User user = this.proxyChat.userManager().user(player);
        Channel channel = ctx.get("channel");

        if (user.unMuteChannel(channel)) {
            player.sendMessage(Messages.COMMAND_CHANNEL_UNMUTED);
        } else {
            player.sendMessage(Messages.COMMAND_CHANNEL_ERROR_NOT_MUTED);
        }
    }
}
