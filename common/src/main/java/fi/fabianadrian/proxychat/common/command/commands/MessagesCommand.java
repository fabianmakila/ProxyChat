package fi.fabianadrian.proxychat.common.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.command.CommandPermission;
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
        var builder = this.commandManager.commandBuilder("messages", "msgs").permission(CommandPermission.MESSAGES.permission()).senderType(User.class);

        this.commandManager.command(builder.literal("spy")
            .permission(CommandPermission.MESSAGES_SPY.permission())
            .argument(BooleanArgument.optional("enabled"))
            .handler(this::executeSpy)
        );

        this.commandManager.command(builder.literal("allow")
            .permission(CommandPermission.MESSAGES_ALLOW.permission())
            .argument(EnumArgument.of(User.MessageSetting.class, "messageSetting"))
            .handler(this::executeAllow)
        );
    }

    private void executeSpy(CommandContext<Commander> ctx) {
        Optional<Boolean> enabledOptional = ctx.getOptional("enabled");
        User user = (User) ctx.getSender();
        boolean value = enabledOptional.orElseGet(() -> !user.spying());

        user.spying(value);
        ctx.getSender().sendMessage(value ? Messages.COMMAND_MESSAGES_SPY_ENABLE : Messages.COMMAND_MESSAGES_SPY_DISABLE);
    }

    private void executeAllow(CommandContext<Commander> ctx) {
        User user = (User) ctx.getSender();
        User.MessageSetting setting = ctx.get("messageSetting");

        user.messageSetting(setting);

        switch (setting) {
            case NOBODY:
                user.sendMessage(Messages.COMMAND_MESSAGES_ALLOW_NOBODY);
                break;
            case FRIENDS:
                user.sendMessage(Messages.COMMAND_MESSAGES_ALLOW_FRIENDS);
                break;
            case EVERYONE:
                user.sendMessage(Messages.COMMAND_MESSAGES_ALLOW_EVERYONE);
                break;
        }
    }
}
