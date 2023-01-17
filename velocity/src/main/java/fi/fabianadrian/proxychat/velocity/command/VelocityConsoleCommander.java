package fi.fabianadrian.proxychat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import fi.fabianadrian.proxychat.common.command.Commander;

public class VelocityConsoleCommander implements Commander {
    private final CommandSource commandSource;

    public VelocityConsoleCommander(CommandSource commandSource) {
        this.commandSource = commandSource;
    }

    public CommandSource commandSource() {

    }
}
