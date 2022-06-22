package fi.fabianadrian.proxychat.service;

import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.channel.Channel;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.format.FormatComponentProvider;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;
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

    public void sendPrivateMessage(Player sender, Player receiver, String message) {
        User senderUser = this.proxyChat.userManager().user(sender.getUniqueId());
        User receiverUser = this.proxyChat.userManager().user(receiver.getUniqueId());

        if (senderUser == receiverUser) {
            senderUser.player().sendMessage(Messages.COMMAND_MESSAGE_ERROR_SELF);
            return;
        }

        if (!sender.hasPermission(CommandPermissions.MESSAGES_TOGGLE_OVERRIDE)) {
            if (!senderUser.messages()) {
                sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_SELF_DISABLE);
                return;
            }
            if (!receiverUser.messages()) {
                sender.sendMessage(Messages.COMMAND_MESSAGE_ERROR_TARGET_DISABLE);
                return;
            }
        }

        // Message components
        Component senderComponent = componentProvider.messageSenderComponent(receiverUser.player().getUsername(), message);
        Component receiverComponent = componentProvider.messageReceiverComponent(senderUser.player().getUsername(), message);
        Component spyComponent = componentProvider.messageSpyComponent(
                senderUser.player().getUsername(),
                receiverUser.player().getUsername(),
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
            user.player().sendMessage(spyComponent);
        }
    }

    public void sendChannelMessage(Channel channel, Player sender, String message) {
        Component component = miniMessage.deserialize(
                channel.format(),
                TagResolver.resolver(
                        Placeholder.unparsed("sender", sender.getUsername()),
                        Placeholder.unparsed("message", message)
                )
        );

        for (User user : this.proxyChat.userManager().users()) {
            if (!user.player().hasPermission(channel.permission())) continue;
            user.player().sendMessage(sender, component);
        }
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
