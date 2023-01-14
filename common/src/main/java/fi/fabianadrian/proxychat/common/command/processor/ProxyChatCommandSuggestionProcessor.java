package fi.fabianadrian.proxychat.common.command.processor;

import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public final class ProxyChatCommandSuggestionProcessor<C> implements CommandSuggestionProcessor<C> {

    @Override
    public @NonNull List<String> apply(
            @NonNull CommandPreprocessingContext<C> context,
            @NonNull List<String> strings
    ) {
        final String input;
        if (context.getInputQueue().isEmpty()) {
            input = "";
        } else {
            input = context.getInputQueue().peek().toLowerCase(Locale.ROOT);
        }
        final List<String> suggestions = new LinkedList<>();
        for (final String suggestion : strings) {
            if (suggestion.toLowerCase(Locale.ROOT).startsWith(input)) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }
}
