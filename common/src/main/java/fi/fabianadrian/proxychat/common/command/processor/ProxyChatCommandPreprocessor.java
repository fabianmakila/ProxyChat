package fi.fabianadrian.proxychat.common.command.processor;

import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.ProxyChatContextKeys;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessor;

public final class ProxyChatCommandPreprocessor<C> implements CommandPreprocessor<C> {
	private final ProxyChat proxyChat;

	public ProxyChatCommandPreprocessor(final ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
	}

	@Override
	public void accept(@NonNull CommandPreprocessingContext<C> context) {
		CommandContext<C> commandContext = context.commandContext();
		commandContext.store(ProxyChatContextKeys.CHANNEL_REGISTRY_KEY, this.proxyChat.channelRegistry());
		commandContext.store(ProxyChatContextKeys.HOOK_MANAGER_KEY, this.proxyChat.platform().hookManager());
		commandContext.store(ProxyChatContextKeys.USER_MANAGER_KEY, this.proxyChat.userManager());
	}
}
