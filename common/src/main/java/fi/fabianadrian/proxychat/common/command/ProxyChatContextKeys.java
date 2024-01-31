package fi.fabianadrian.proxychat.common.command;

import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.user.UserManager;
import io.leangen.geantyref.TypeToken;
import org.incendo.cloud.key.CloudKey;

public class ProxyChatContextKeys {

	public static final CloudKey<ChannelRegistry> CHANNEL_REGISTRY_KEY = CloudKey.of(
			"ChannelRegistry",
			TypeToken.get(ChannelRegistry.class)
	);
	public static final CloudKey<HookManager> HOOK_MANAGER_KEY = CloudKey.of(
			"HookManager",
			TypeToken.get(HookManager.class)
	);
	public static final CloudKey<UserManager> USER_MANAGER_KEY = CloudKey.of(
			"UserManager",
			TypeToken.get(UserManager.class)
	);

	private ProxyChatContextKeys() {
	}
}
