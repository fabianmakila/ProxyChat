package fi.fabianadrian.proxychat.common.command;

import net.kyori.adventure.audience.ForwardingAudience;

public interface Commander extends ForwardingAudience.Single {
    boolean hasPermission(String permission);
}
