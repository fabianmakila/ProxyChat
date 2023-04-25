package fi.fabianadrian.proxychat.common.locale;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface Messages {
    Component PREFIX_COMPONENT = MiniMessage.miniMessage().deserialize("<white>[<aqua>ProxyChat</aqua>]</white> ");

    Component COMMAND_ANNOUNCEMENTS_OFF = translatable()
            .key("proxychat.command.announcements.off")
            .color(RED)
            .build();
    Component COMMAND_ANNOUNCEMENTS_ON = translatable()
            .key("proxychat.command.announcements.on")
            .color(GREEN)
            .build();
    Component COMMAND_CHANNEL_ERROR_ALREADY_MUTED = translatable()
            .key("proxychat.command.channel.error.already-muted")
            .color(RED)
            .build();
    Component COMMAND_CHANNEL_ERROR_NOT_MUTED = translatable()
            .key("proxychat.command.channel.error.not-muted")
            .color(RED)
            .build();
    Component COMMAND_CHANNEL_LIST = translatable()
            .key("proxychat.command.channel.list")
            .color(GREEN)
            .build();
    Component COMMAND_CHANNEL_MUTED = translatable()
            .key("proxychat.command.channel.muted")
            .color(GREEN)
            .build();
    Component COMMAND_CHANNEL_UNMUTED = translatable()
            .key("proxychat.command.channel.unmuted")
            .color(GREEN)
            .build();
    Component COMMAND_MESSAGE_ERROR_SELF = translatable()
            .key("proxychat.command.message.error.self")
            .color(RED)
            .build();
    Component COMMAND_MESSAGE_ERROR_DISALLOWED = translatable()
            .key("proxychat.command.message.error.disallowed")
            .color(RED)
            .build();
    Component COMMAND_MESSAGES_ALLOW_NOBODY = translatable()
            .key("proxychat.command.messages.allow.nobody")
            .color(GREEN)
            .build();
    Component COMMAND_MESSAGES_ALLOW_FRIENDS = translatable()
            .key("proxychat.command.messages.allow.friends")
            .color(GREEN)
            .build();
    Component COMMAND_MESSAGES_ALLOW_EVERYONE = translatable()
        .key("proxychat.command.messages.allow.everyone")
        .color(GREEN)
        .build();
    Component COMMAND_MESSAGES_SPY_DISABLE = translatable()
        .key("proxychat.command.messages.spy.disable")
        .color(RED)
        .build();
    Component COMMAND_MESSAGES_SPY_ENABLE = translatable()
        .key("proxychat.command.messages.spy.enable")
        .color(GREEN)
        .build();
    Component COMMAND_PROXYCHAT_RELOAD_SUCCESS = translatable()
            .key("proxychat.command.proxychat.reload.success")
            .color(GREEN)
            .build();
    Component COMMAND_REPLY_ERROR_NO_LAST_MESSAGED = translatable()
            .key("proxychat.command.reply.error.no-last-messaged")
            .color(RED)
            .build();
    Component COMMAND_REPLY_ERROR_LAST_MESSAGED_OFFLINE = translatable()
            .key("proxychat.command.reply.error.last-messaged-offline")
            .color(RED)
            .build();

    Component GENERAL_ME = translatable("proxychat.general.me");

    static TextComponent prefixed(ComponentLike component) {
        return text()
                .append(PREFIX_COMPONENT)
                .append(Component.space())
                .append(component)
                .build();
    }
}
