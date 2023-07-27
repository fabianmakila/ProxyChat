package fi.fabianadrian.proxychat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import fi.fabianadrian.proxychat.common.command.Commander;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public class VelocityConsoleCommander implements Commander {

	private final CommandSource commandSource;

	public VelocityConsoleCommander(CommandSource commandSource) {
		this.commandSource = commandSource;
	}

	public CommandSource commandSource() {
		return this.commandSource;
	}

	@Override
	public boolean hasPermission(String permission) {
		return this.commandSource.hasPermission(permission);
	}

	@Override
	public @NotNull Audience audience() {
		return this.commandSource;
	}
}
