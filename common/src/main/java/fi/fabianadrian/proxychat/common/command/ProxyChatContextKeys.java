package fi.fabianadrian.proxychat.common.command;

import cloud.commandframework.keys.CloudKey;
import cloud.commandframework.keys.SimpleCloudKey;
import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.user.UserManager;
import io.leangen.geantyref.TypeToken;

public class ProxyChatContextKeys {

    private ProxyChatContextKeys() {}

    public static final CloudKey<ChannelRegistry> CHANNEL_REGISTRY_KEY = SimpleCloudKey.of(
        "ChannelRegistry",
        TypeToken.get(ChannelRegistry.class)
    );
    public static final CloudKey<HookManager> HOOK_MANAGER_KEY = SimpleCloudKey.of(
        "HookManager",
        TypeToken.get(HookManager.class)
    );
    public static final CloudKey<UserManager> USER_MANAGER_KEY = SimpleCloudKey.of(
        "ProxyServer",
        TypeToken.get(UserManager.class)
    );
}
