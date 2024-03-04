package fi.fabianadrian.proxychat.bungeecord.command;

import fi.fabianadrian.proxychat.common.command.Commander;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

public record BungeecordConsoleCommander(CommandSender commandSender, Audience audience) implements Commander {

	@Override
	public boolean hasPermission(String permission) {
		return this.commandSender.hasPermission(permission);
	}

	@Override
	public @NotNull Audience audience() {
		return this.audience;
	}
}
