package fi.fabianadrian.proxychat.bungeecord.listener;

import fi.fabianadrian.proxychat.bungeecord.BungeecordPlatformPlayer;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.user.User;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginDisconnectListener implements Listener {

    private final ProxyChat proxyChat;

    public LoginDisconnectListener(ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        BungeecordPlatformPlayer platformPlayer = new BungeecordPlatformPlayer(event.getPlayer());
        User user = this.proxyChat.userManager().loadUser(platformPlayer);
        this.proxyChat.messageService().sendWelcomeMessage(user);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        this.proxyChat.userManager().unloadUser(event.getPlayer().getUniqueId());
    }
}
