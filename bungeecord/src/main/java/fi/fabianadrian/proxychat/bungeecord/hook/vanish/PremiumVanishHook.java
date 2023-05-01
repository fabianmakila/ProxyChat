package fi.fabianadrian.proxychat.bungeecord.hook.vanish;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import fi.fabianadrian.proxychat.bungeecord.BungeecordPlatformPlayer;
import fi.fabianadrian.proxychat.common.hook.vanish.VanishHook;
import fi.fabianadrian.proxychat.common.user.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PremiumVanishHook implements VanishHook {

    @Override
    public boolean canSee(User user1, User user2) {
        ProxiedPlayer player1 = ((BungeecordPlatformPlayer) user1.player()).player();
        ProxiedPlayer player2 = ((BungeecordPlatformPlayer) user2.player()).player();
        return BungeeVanishAPI.canSee(player1, player2);
    }
}
