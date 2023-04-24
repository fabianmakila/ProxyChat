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
import fi.fabianadrian.proxychat.common.user.User;
import fi.fabianadrian.proxychat.common.user.UserManager;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Argument parse for {@link User channels}
 *
 * @param <C> Command sender type
 */
public final class UserArgument<C> extends CommandArgument<C, User> {

    private UserArgument(final boolean required, final @NotNull String name, final @NotNull String defaultValue, final @Nullable BiFunction<CommandContext<C>, String, List<String>> suggestionsProvider, final @NotNull ArgumentDescription defaultDescription, final @NotNull Collection<@NotNull BiFunction<@NotNull CommandContext<C>, @NotNull Queue<@NotNull String>, @NotNull ArgumentParseResult<Boolean>>> argumentPreprocessors) {
        super(required, name, new UserParser<>(), defaultValue, TypeToken.get(User.class), suggestionsProvider, defaultDescription, argumentPreprocessors);
    }

    /**
     * Create a new argument builder
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Constructed builder
     */
    public static <C> CommandArgument.@NotNull Builder<C, User> newBuilder(final @NotNull String name) {
        return new Builder<C>(name).withParser(new UserParser<>());
    }

    /**
     * Create a new required server argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NotNull CommandArgument<C, User> of(final @NotNull String name) {
        return UserArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional server argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NotNull CommandArgument<C, User> optional(final @NotNull String name) {
        return UserArgument.<C>newBuilder(name).asOptional().build();
    }

    public static final class Builder<C> extends CommandArgument.Builder<C, User> {

        private Builder(final @NotNull String name) {
            super(TypeToken.get(User.class), name);
        }

        @Override
        public @NotNull CommandArgument<@NotNull C, @NotNull User> build() {
            return new UserArgument<>(this.isRequired(), this.getName(), this.getDefaultValue(), this.getSuggestionsProvider(), this.getDefaultDescription(), new LinkedList<>());
        }

    }

    public static final class UserParser<C> implements ArgumentParser<C, User> {
        @Override
        public @NotNull ArgumentParseResult<@NotNull User> parse(final @NotNull CommandContext<@NotNull C> commandContext, final @NotNull Queue<@NotNull String> inputQueue) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(UserParser.class, commandContext));
            }
            final Optional<User> userOptional = commandContext.<UserManager>get("UserManager").user(input);
            if (userOptional.isEmpty()) {
                return ArgumentParseResult.failure(new UserParseException(input, commandContext));
            }
            inputQueue.remove();
            return ArgumentParseResult.success(userOptional.get());
        }

        @Override
        public @NotNull List<@NotNull String> suggestions(final @NotNull CommandContext<C> ctx, final @NotNull String input) {
            UserManager userManager = ctx.get("UserManager");
            return userManager.users().stream().map(User::name).collect(Collectors.toList());
        }
    }

    //TODO How do caption keys work????
    public static final class UserParseException extends ParserException {
        private static final long serialVersionUID = -3171394255739625192L;

        private UserParseException(final @NotNull String input, final @NotNull CommandContext<?> context) {
            super(UserParser.class, context, Caption.of("argument.parse.failure.channel"), CaptionVariable.of("input", input));
        }
    }
}
