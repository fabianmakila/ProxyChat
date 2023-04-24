package fi.fabianadrian.proxychat.common.user;

import net.kyori.adventure.audience.ForwardingAudience;

import java.util.UUID;

public interface PlatformPlayer extends ForwardingAudience.Single {
    UUID uuid();

    String name();

    boolean hasPermission(String permission);
}
