package fi.fabianadrian.proxychat.common.platform;

import fi.fabianadrian.conversation.common.platform.Platform;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.dependency.ProxyDependencyManager;
import net.kyori.adventure.audience.ForwardingAudience;
import org.incendo.cloud.CommandManager;

public interface ProxyPlatform extends Platform, ForwardingAudience.Single {
	CommandManager<Commander> commandManager();

	ProxyDependencyManager dependencyManager();
}
