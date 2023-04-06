package fi.fabianadrian.proxychat.common.service;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.api.event.ChannelMessageEvent;
import fi.fabianadrian.proxychat.api.channel.Channel;
import fi.fabianadrian.proxychat.common.command.CommandPermissions;
import fi.fabianadrian.proxychat.common.format.FormatComponentProvider;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;

public final class MessageService {

    private final ProxyChat proxyChat;
    private final FormatComponentProvider componentProvider;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MessageService(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.componentProvider = proxyChat.formatComponentProvider();
    }

    public void sendPrivateMessage(User sender, User receiver, String message) {

        if (sender == receiver) {
            sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_SELF);
            return;
        }

        if (!sender.hasPermission(CommandPermissions.MESSAGES_TOGGLE_OVERRIDE.permission())) {
            if (!sender.allowMessages()) {
                sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_SELF_DISABLE);
                return;
            }
            if (!receiver.allowMessages()) {
                sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_TARGET_DISABLE);
                return;
            }
        }

        // Message components
        Component senderComponent = componentProvider.messageSenderComponent(receiver.base().name(), message);
        Component receiverComponent = componentProvider.messageReceiverComponent(sender.base().name(), message);
        Component spyComponent = componentProvider.messageSpyComponent(
                sender.base().name(),
                receiver.base().name(),
                message
        );

        // Send components
        sender.sendMessage(senderComponent);
        receiver.sendMessage(senderUser.player(), receiverComponent);

        receiverUser.lastMessaged(sender.getUniqueId());

        //TODO Send spy component to console if configured

        //TODO Optimize this?
        //Sends message spy component to every online user that has spy true
        for (User user : this.proxyChat.userManager().users()) {
            if (user == senderUser || user == receiverUser || !user.spying()) continue;
            user.base().sendMessage(spyComponent);
        }
    }

    public void sendChannelMessage(Channel channel, Player sender, String message) {
        this.proxyChat.proxyServer().getEventManager().fire(new ChannelMessageEvent(channel, sender, message)).thenAcceptAsync((event) -> {
            if (event.getResult() == ResultedEvent.GenericResult.denied()) return;

            Component component = miniMessage.deserialize(
                    channel.format(),
                    TagResolver.resolver(
                            Placeholder.unparsed("sender", sender.getUsername()),
                            Placeholder.unparsed("message", message)
                    )
            );

            for (User user : this.proxyChat.userManager().users()) {
                if (!user.base().hasPermission(channel.permission())) continue;
                user.base().sendMessage(sender, component);
            }
        });
    }

    public void sendWelcomeMessage(Player player) {
        List<String> rawLines = this.proxyChat.configManager().mainConfig().welcomeMessage();

        if (rawLines.isEmpty()) return;

        List<Component> lines = new ArrayList<>();
        rawLines.forEach(line -> lines.add(miniMessage.deserialize(line, TagResolver.resolver(
                Placeholder.unparsed("name", player.getUsername())
        ))));

        player.sendMessage(Component.join(JoinConfiguration.newlines(), lines));
    }
}