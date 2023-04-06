package fi.fabianadrian.proxychat.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.user.User;

import java.util.Optional;

public final class ChatListener {

    private final ProxyChat proxyChat;

    public ChatListener(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        Optional<User> userOptional = this.proxyChat.userManager().user(event.getPlayer().getUniqueId());

        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();

        String selectedChannel = user.selectedChannel();
        if (selectedChannel == null) {
            return;
        }

        event.setResult(PlayerChatEvent.ChatResult.denied());

        Channel channel = this.proxyChat.channelRegistry().channel(selectedChannel);
        this.proxyChat.messageService().sendChannelMessage(channel, user, event.getMessage());
    }
}
