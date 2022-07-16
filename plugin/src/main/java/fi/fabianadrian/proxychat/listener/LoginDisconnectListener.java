package fi.fabianadrian.proxychat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;

public final class LoginDisconnectListener {

    private final ProxyChat proxyChat;

    public LoginDisconnectListener(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        this.proxyChat.userManager().loadUser(player);
        this.proxyChat.messageService().sendWelcomeMessage(player);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (event.getLoginStatus() != DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN) return;
        this.proxyChat.userManager().unloadUser(event.getPlayer().getUniqueId());
    }
}
