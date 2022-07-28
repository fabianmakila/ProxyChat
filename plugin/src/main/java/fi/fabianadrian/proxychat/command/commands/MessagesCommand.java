package fi.fabianadrian.proxychat.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.command.AbstractCommand;
import fi.fabianadrian.proxychat.command.CommandPermissions;
import fi.fabianadrian.proxychat.locale.Messages;
import fi.fabianadrian.proxychat.user.User;

import java.util.Optional;

public class MessagesCommand extends AbstractCommand {

    public MessagesCommand(ProxyChat proxyChat) {
        super(proxyChat);
    }

    @Override
    public void register() {
        var builder = this.commandManager.commandBuilder("messages", "msgs").senderType(Player.class);

        this.commandManager.command(builder.literal("spy").permission(CommandPermissions.MESSAGES_SPY).argument(BooleanArgument.optional("enabled")).handler(this::executeSpy));
        this.commandManager.command(builder.literal("toggle").permission(CommandPermissions.MESSAGES_TOGGLE).argument(BooleanArgument.optional("enabled")).handler(this::executeToggle));
    }

    private void executeSpy(CommandContext<CommandSource> ctx) {
        Optional<Boolean> enabledOptional = ctx.getOptional("enabled");
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());
        boolean value = enabledOptional.orElseGet(() -> !user.spying());

        user.spying(value);
        ctx.getSender().sendMessage(value ? Messages.COMMAND_MESSAGES_SPY_ENABLE : Messages.COMMAND_MESSAGES_SPY_DISABLE);
    }

    private void executeToggle(CommandContext<CommandSource> ctx) {
        Optional<Boolean> enabledOptional = ctx.getOptional("enabled");
        User user = this.proxyChat.userManager().user((Player) ctx.getSender());
        boolean value = enabledOptional.orElseGet(() -> !user.messages());

        user.messages(value);
        ctx.getSender().sendMessage(value ? Messages.COMMAND_MESSAGES_TOGGLE_ENABLE : Messages.COMMAND_MESSAGES_TOGGLE_DISABLE);
    }
}
