package fi.fabianadrian.proxychat.common.command.parser;

import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatContextKeys;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import fi.fabianadrian.proxychat.common.hook.VanishPluginHook;
import fi.fabianadrian.proxychat.common.user.User;
import fi.fabianadrian.proxychat.common.user.UserManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.minecraft.extras.caption.TranslatableCaption;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UserParser implements ArgumentParser<Commander, User>, BlockingSuggestionProvider.Strings<Commander> {

	public static @NonNull ParserDescriptor<Commander, User> userParser() {
		return ParserDescriptor.of(new UserParser(), User.class);
	}

	public static CommandComponent.@NonNull Builder<Commander, User> userComponent() {
		return CommandComponent.<Commander, User>builder().parser(userParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull User> parse(@NonNull CommandContext<@NonNull Commander> context, @NonNull CommandInput input) {
		UserManager userManager = context.get(ProxyChatContextKeys.USER_MANAGER_KEY);
		HookManager hookManager = context.get(ProxyChatContextKeys.HOOK_MANAGER_KEY);

		final String inputString = input.peekString(); // Does not remove the string from the input!

		final Optional<User> userOptional = userManager.user(inputString);
		if (userOptional.isEmpty()) {
			return ArgumentParseResult.failure(new UserParseException(inputString, context));
		}
		User user = userOptional.get();

		Optional<VanishPluginHook> vanishHookOptional = hookManager.vanishHook();
		if (vanishHookOptional.isPresent() && context.sender() instanceof User) {
			if (context.sender().equals(user) || !vanishHookOptional.get().canSee((User) context.sender(), user)) {
				return ArgumentParseResult.failure(new UserParseException(inputString, context));
			}
		}

		input.readString();

		return ArgumentParseResult.success(user);
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<Commander> context, @NonNull CommandInput input) {
		UserManager userManager = context.get(ProxyChatContextKeys.USER_MANAGER_KEY);
		HookManager hookManager = context.get(ProxyChatContextKeys.HOOK_MANAGER_KEY);

		Stream<User> users = userManager.users().stream();
		Optional<VanishPluginHook> vanishHookOptional = hookManager.vanishHook();

		if (context.sender() instanceof User) {
			users = users.filter(user -> user != context.sender());

			if (vanishHookOptional.isPresent()) {
				users = users.filter(user -> vanishHookOptional.get().canSee((User) context.sender(), user));
			}
		}

		return users.map(User::name).collect(Collectors.toList());
	}

	public static final class UserParseException extends ParserException {
		private UserParseException(
				final @NonNull String input,
				final @NonNull CommandContext<?> context
		) {
			super(
					UserParser.class,
					context,
					TranslatableCaption.translatableCaption("argument.parse.failure.user"),
					CaptionVariable.of("input", input)
			);
		}
	}
}
