package fi.fabianadrian.proxychat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import fi.fabianadrian.proxychat.user.UserManager;

public final class LoginDisconnectListener {

    private final UserManager userManager;

    public LoginDisconnectListener(UserManager userManager) {
        this.userManager = userManager;
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        this.userManager.loadUser(event.getPlayer());
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (event.getLoginStatus() != DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN) return;
        this.userManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
