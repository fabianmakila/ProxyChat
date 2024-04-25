package fi.fabianadrian.proxychat.common.command.parser;

import fi.fabianadrian.proxychat.common.channel.Channel;
import fi.fabianadrian.proxychat.common.channel.ChannelRegistry;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.command.ProxyChatContextKeys;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.stream.Collectors;

public final class ChannelParser implements ArgumentParser<Commander, Channel>, BlockingSuggestionProvider.Strings<Commander> {
	public static @NonNull ParserDescriptor<Commander, Channel> channelParser() {
		return ParserDescriptor.of(new ChannelParser(), Channel.class);
	}

	public static CommandComponent.@NonNull Builder<Commander, Channel> channelComponent() {
		return CommandComponent.<Commander, Channel>builder().parser(channelParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull Channel> parse(@NonNull CommandContext<@NonNull Commander> context, @NonNull CommandInput input) {
		final ChannelRegistry channelRegistry = context.get(ProxyChatContextKeys.CHANNEL_REGISTRY_KEY);

		final String inputString = input.peekString();
		final Channel channel = channelRegistry.channel(inputString);
		if (channel == null) {
			return ArgumentParseResult.failure(new ChannelParseException(inputString, context));
		}
		input.readString();
		return ArgumentParseResult.success(channel);
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<Commander> context, @NonNull CommandInput input) {
		final ChannelRegistry channelRegistry = context.get(ProxyChatContextKeys.CHANNEL_REGISTRY_KEY);
		return channelRegistry.channels().stream().filter(channel -> context.sender().hasPermission(channel.permission())).map(Channel::name).collect(Collectors.toList());
	}

	public static final class ChannelParseException extends ParserException {
		private ChannelParseException(
				final @NonNull String input,
				final @NonNull CommandContext<?> context
		) {
			super(
					ChannelParser.class,
					context,
					Caption.of("argument.parse.failure.channel"),
					CaptionVariable.of("input", input)
			);
		}
	}
}
