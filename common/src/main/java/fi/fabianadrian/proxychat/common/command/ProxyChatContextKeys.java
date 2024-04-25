package fi.fabianadrian.proxychat.common.command;

import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.user.UserManager;
import org.incendo.cloud.key.CloudKey;

public final class ProxyChatContextKeys {
	public static final CloudKey<ChannelRegistry> CHANNEL_REGISTRY_KEY = CloudKey.of("ChannelRegistry", ChannelRegistry.class);
	public static final CloudKey<HookManager> HOOK_MANAGER_KEY = CloudKey.of("HookManager", HookManager.class);
	public static final CloudKey<UserManager> USER_MANAGER_KEY = CloudKey.of("UserManager", UserManager.class);

	private ProxyChatContextKeys() {
	}
}
