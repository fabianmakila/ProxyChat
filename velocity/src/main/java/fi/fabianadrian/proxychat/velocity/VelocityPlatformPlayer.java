package fi.fabianadrian.proxychat.velocity;

import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.common.platform.PlatformPlayer;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record VelocityPlatformPlayer(Player player) implements PlatformPlayer {
	@Override
	public UUID uuid() {
		return this.player.getUniqueId();
	}

	@Override
	public String name() {
		return this.player.getUsername();
	}

	@Override
	public boolean hasPermission(String permission) {
		return this.player.hasPermission(permission);
	}

	@Override
	public @NotNull Audience audience() {
		return this.player;
	}
}
