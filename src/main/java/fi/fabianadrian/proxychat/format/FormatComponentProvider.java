package fi.fabianadrian.proxychat.format;

import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.config.ProxyChatConfig;
import fi.fabianadrian.proxychat.locale.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

//TODO Figure out better name(?)
public final class FormatComponentProvider {

    private final ProxyChatConfig mainConfig;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public FormatComponentProvider(ProxyChat proxyChat) {
        this.mainConfig = proxyChat.configManager().mainConfig();
    }

    public Component broadcastComponent(String message) {
        String format = this.mainConfig.formats().broadcast();
        return this.miniMessage.deserialize(
                format,
                TagResolver.resolver(
                        Placeholder.unparsed("message", message)
                )
        );
    }

    public Component messageSenderComponent(String receiverName, String message) {
        return this.miniMessage.deserialize(
                this.mainConfig.formats().msg(),
                TagResolver.resolver(
                        Placeholder.component("sender", Messages.GENERAL_ME),
                        Placeholder.unparsed("receiver", receiverName),
                        Placeholder.unparsed("message", message)
                )
        );
    }

    public Component messageReceiverComponent(String senderName, String message) {
        return this.miniMessage.deserialize(
                this.mainConfig.formats().msg(),
                TagResolver.resolver(
                        Placeholder.unparsed("sender", senderName),
                        Placeholder.component("receiver", Messages.GENERAL_ME),
                        Placeholder.unparsed("message", message)
                )
        );
    }

    public Component messageSpyComponent(String senderName, String receiverName, String message) {
        return this.miniMessage.deserialize(
                this.mainConfig.formats().msg(),
                TagResolver.resolver(
                        Placeholder.unparsed("sender", senderName),
                        Placeholder.unparsed("receiver", receiverName),
                        Placeholder.unparsed("message", message)
                )
        );
    }
}
