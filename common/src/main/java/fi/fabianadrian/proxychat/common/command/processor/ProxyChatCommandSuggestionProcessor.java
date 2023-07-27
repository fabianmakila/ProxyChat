package fi.fabianadrian.proxychat.common.command.processor;

import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class ProxyChatCommandSuggestionProcessor<C> implements CommandSuggestionProcessor<C> {

	@Override
	public @NonNull List<String> apply(
			@NonNull CommandPreprocessingContext<C> context,
			@NonNull List<String> strings
	) {
		final String input = context.getInputQueue().isEmpty() ? "" : context.getInputQueue().peek().toLowerCase(Locale.ROOT);
		return strings.stream()
				.filter(suggestion -> suggestion.toLowerCase(Locale.ROOT).startsWith(input))
				.collect(Collectors.toList());
	}
}
