package fi.fabianadrian.proxychat.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.platform.PlatformPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

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
	public Component currentServerName() {
		Optional<ServerConnection> serverOptional = this.player.getCurrentServer();
		if (serverOptional.isEmpty()) {
			return Messages.GENERAL_UNKNOWN;
		}
		return text(serverOptional.get().getServerInfo().getName());
	}

	@Override
	public @NotNull Audience audience() {
		return this.player;
	}
}
