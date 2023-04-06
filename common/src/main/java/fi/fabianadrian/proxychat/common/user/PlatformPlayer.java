package fi.fabianadrian.proxychat.common.user;

import net.kyori.adventure.audience.Audience;

import java.util.UUID;

public interface PlatformPlayer extends Audience {
    UUID uuid();
    String name();
    boolean hasPermission(String permission);
}
