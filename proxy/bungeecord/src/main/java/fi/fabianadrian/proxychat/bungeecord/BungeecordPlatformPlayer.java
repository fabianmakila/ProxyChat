package fi.fabianadrian.proxychat.bungeecord;

import fi.fabianadrian.proxychat.common.platform.PlatformPlayer;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record BungeecordPlatformPlayer(ProxiedPlayer player, Audience audience) implements PlatformPlayer {
	@Override
	public UUID uuid() {
		return this.player.getUniqueId();
	}

	@Override
	public String name() {
		return this.player.getName();
	}

	@Override
	public boolean hasPermission(String permission) {
		return this.player.hasPermission(permission);
	}

	@Override
	public @NotNull Audience audience() {
		return this.audience;
	}
}
