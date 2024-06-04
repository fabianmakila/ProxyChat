package fi.fabianadrian.proxychat.common.command;

import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.dependency.ProxyDependencyManager;
import fi.fabianadrian.proxychat.common.user.UserManager;
import org.incendo.cloud.key.CloudKey;

public final class ProxyChatContextKeys {
	public static final CloudKey<ChannelRegistry> CHANNEL_REGISTRY_KEY = CloudKey.of("ChannelRegistry", ChannelRegistry.class);
	public static final CloudKey<ProxyDependencyManager> HOOK_MANAGER_KEY = CloudKey.of("HookManager", ProxyDependencyManager.class);
	public static final CloudKey<UserManager> USER_MANAGER_KEY = CloudKey.of("UserManager", UserManager.class);

	private ProxyChatContextKeys() {
	}
}
