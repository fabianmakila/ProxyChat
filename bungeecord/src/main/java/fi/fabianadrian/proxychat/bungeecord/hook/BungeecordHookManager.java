package fi.fabianadrian.proxychat.bungeecord.hook;

import fi.fabianadrian.proxychat.bungeecord.ProxyChatBungeecord;
import fi.fabianadrian.proxychat.bungeecord.hook.vanish.PremiumVanishHook;
import fi.fabianadrian.proxychat.common.hook.FriendHook;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.hook.vanish.VanishHook;

public class BungeecordHookManager extends HookManager {
    private final VanishHook vanishHook;
    private FriendHook friendHook;

    public BungeecordHookManager(ProxyChatBungeecord plugin) {
        super(plugin.logger());

        try {
            this.friendHook = new PAFBungeecordFriendHook();
            this.logger.info("PartyAndFriends hook enabled!");
        } catch (NoClassDefFoundError e) {
            this.friendHook = FriendHook.empty();
        }

        if (plugin.getProxy().getPluginManager().getPlugin("PremiumVanish") != null) {
            this.vanishHook = new PremiumVanishHook();
        } else {
            this.vanishHook = VanishHook.empty();
        }
    }

    @Override
    public FriendHook friendHook() {
        return this.friendHook;
    }

    @Override
    public VanishHook vanishHook() {
        return this.vanishHook;
    }
}
