package fi.fabianadrian.proxychat.common.command.argument;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.Caption;
import cloud.commandframework.captions.CaptionVariable;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatContextKeys;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Argument parse for {@link Channel channels}
 *
 * @param <C> Command sender type
 */
public final class ChannelArgument<C> extends CommandArgument<C, Channel> {

    private ChannelArgument(final boolean required, final @NotNull String name, final @NotNull String defaultValue, final @Nullable BiFunction<CommandContext<C>, String, List<String>> suggestionsProvider, final @NotNull ArgumentDescription defaultDescription, final @NotNull Collection<@NotNull BiFunction<@NotNull CommandContext<C>, @NotNull Queue<@NotNull String>, @NotNull ArgumentParseResult<Boolean>>> argumentPreprocessors) {
        super(required, name, new ChannelParser<>(), defaultValue, TypeToken.get(Channel.class), suggestionsProvider, defaultDescription, argumentPreprocessors);
    }

    /**
     * Create a new argument builder
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Constructed builder
     */
    public static <C> CommandArgument.@NotNull Builder<C, Channel> newBuilder(final @NotNull String name) {
        return new Builder<C>(name).withParser(new ChannelParser<>());
    }

    /**
     * Create a new required server argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NotNull CommandArgument<C, Channel> of(final @NotNull String name) {
        return ChannelArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional server argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NotNull CommandArgument<C, Channel> optional(final @NotNull String name) {
        return ChannelArgument.<C>newBuilder(name).asOptional().build();
    }

    public static final class Builder<C> extends CommandArgument.Builder<C, Channel> {

        private Builder(final @NotNull String name) {
            super(TypeToken.get(Channel.class), name);
        }

        @Override
        public @NotNull CommandArgument<@NotNull C, @NotNull Channel> build() {
            return new ChannelArgument<>(this.isRequired(), this.getName(), this.getDefaultValue(), this.getSuggestionsProvider(), this.getDefaultDescription(), new LinkedList<>());
        }

    }

    public static final class ChannelParser<C> implements ArgumentParser<C, Channel> {
        @Override
        public @NotNull ArgumentParseResult<@NotNull Channel> parse(final @NotNull CommandContext<@NotNull C> commandContext, final @NotNull Queue<@NotNull String> inputQueue) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(ChannelParser.class, commandContext));
            }
            final Channel channel = commandContext.get(ProxyChatContextKeys.CHANNEL_REGISTRY_KEY).channel(input);
            if (channel == null) {
                return ArgumentParseResult.failure(new ChannelParseException(input, commandContext));
            }
            inputQueue.remove();
            return ArgumentParseResult.success(channel);
        }

        @Override
        public @NotNull List<@NotNull String> suggestions(final @NotNull CommandContext<C> ctx, final @NotNull String input) {
            Commander commander = (Commander) ctx.getSender();
            ChannelRegistry registry = ctx.get(ProxyChatContextKeys.CHANNEL_REGISTRY_KEY);

            return registry.channels().stream().filter(channel -> commander.hasPermission(channel.permission())).map(Channel::name).collect(Collectors.toList());
        }
    }

    public static final class ChannelParseException extends ParserException {
        private static final long serialVersionUID = -2318348844932495100L;

        private ChannelParseException(final @NotNull String input, final @NotNull CommandContext<?> context) {
            super(ChannelParser.class, context, Caption.of("argument.parse.failure.channel"), CaptionVariable.of("input", input));
        }
    }
}
