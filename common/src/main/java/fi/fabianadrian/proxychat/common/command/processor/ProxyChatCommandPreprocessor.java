package fi.fabianadrian.proxychat.common.command.processor;

import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import cloud.commandframework.execution.preprocessor.CommandPreprocessor;
import fi.fabianadrian.proxychat.common.ProxyChat;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ProxyChatCommandPreprocessor<C> implements CommandPreprocessor<C> {
    private final ProxyChat proxyChat;

    public ProxyChatCommandPreprocessor(final ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
    }

    @Override
    public void accept(@NonNull CommandPreprocessingContext<C> context) {
        context.getCommandContext().store("ChannelRegistry", this.proxyChat.channelRegistry());
        context.getCommandContext().store("UserManager", this.proxyChat.userManager());
    }
}
