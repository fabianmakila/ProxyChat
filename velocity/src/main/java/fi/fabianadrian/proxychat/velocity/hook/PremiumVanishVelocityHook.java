package fi.fabianadrian.proxychat.velocity.hook;

import com.velocitypowered.api.proxy.Player;
import de.myzelyam.api.vanish.VelocityVanishAPI;
import fi.fabianadrian.proxychat.common.hook.VanishPluginHook;
import fi.fabianadrian.proxychat.common.user.User;
import fi.fabianadrian.proxychat.velocity.VelocityPlatformPlayer;

public final class PremiumVanishVelocityHook implements VanishPluginHook {
	@Override
	public boolean canSee(User user1, User user2) {
		Player player1 = ((VelocityPlatformPlayer) user1.player()).player();
		Player player2 = ((VelocityPlatformPlayer) user2.player()).player();
		return VelocityVanishAPI.canSee(player1, player2);
	}

	@Override
	public String pluginName() {
		return "PremiumVanish";
	}
}
