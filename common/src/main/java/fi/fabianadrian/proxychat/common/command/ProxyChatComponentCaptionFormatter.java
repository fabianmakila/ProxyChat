package fi.fabianadrian.proxychat.common.command;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.minecraft.extras.caption.ComponentCaptionFormatter;
import org.incendo.cloud.minecraft.extras.caption.RichVariable;

import java.util.List;
import java.util.stream.Collectors;

public final class ProxyChatComponentCaptionFormatter {
	public static <C> @NonNull ComponentCaptionFormatter<C> translatable() {
		return new ComponentCaptionFormatter<>() {
			@Override
			public @NonNull Component formatCaption(
					final @NonNull Caption captionKey,
					final @NonNull C recipient,
					final @NonNull String caption,
					final @NonNull List<@NonNull CaptionVariable> variables
			) {
				return Component.translatable("cloud." + captionKey.key(), variables.stream().map(variable -> {
					if (variable instanceof RichVariable) {
						return (RichVariable) variable;
					} else {
						return Component.text(variable.value());
					}
				}).collect(Collectors.toList()));
			}
		};
	}
}
