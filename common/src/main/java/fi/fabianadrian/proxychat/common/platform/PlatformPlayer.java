package fi.fabianadrian.proxychat.common.platform;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface PlatformPlayer extends ForwardingAudience.Single {
	UUID uuid();

	String name();

	boolean hasPermission(String permission);

	Component currentServerName();
}
