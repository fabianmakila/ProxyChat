package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.CommandPermissions;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatCommand;
import fi.fabianadrian.proxychat.common.locale.Messages;
import fi.fabianadrian.proxychat.common.user.User;

import java.util.Optional;

public class MessagesCommand extends ProxyChatCommand {

    public MessagesCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("messages", "msgs").permission(CommandPermissions.MESSAGES.permission()).senderType(User.class);

        this.commandManager.command(builder.literal("spy")
            .permission(CommandPermissions.MESSAGES_SPY.permission())
            .argument(BooleanArgument.optional("enabled"))
            .handler(this::executeSpy));

        this.commandManager.command(builder.literal("toggle")
            .permission(CommandPermissions.MESSAGES_TOGGLE.permission())
            .argument(BooleanArgument.optional("enabled"))
            .handler(this::executeToggle));
    }

    private void executeSpy(CommandContext<Commander> ctx) {
        Optional<Boolean> enabledOptional = ctx.getOptional("enabled");
        User user = (User) ctx.getSender();
        boolean value = enabledOptional.orElseGet(() -> !user.spying());

        user.spying(value);
        ctx.getSender().sendMessage(value ? Messages.COMMAND_MESSAGES_SPY_ENABLE : Messages.COMMAND_MESSAGES_SPY_DISABLE);
    }

    private void executeToggle(CommandContext<Commander> ctx) {
        Optional<Boolean> enabledOptional = ctx.getOptional("enabled");
        User user = (User) ctx.getSender();
        boolean value = enabledOptional.orElseGet(() -> !user.allowMessages());

        user.allowMessages(value);
        ctx.getSender().sendMessage(value ? Messages.COMMAND_MESSAGES_TOGGLE_ENABLE : Messages.COMMAND_MESSAGES_TOGGLE_DISABLE);
    }
}
