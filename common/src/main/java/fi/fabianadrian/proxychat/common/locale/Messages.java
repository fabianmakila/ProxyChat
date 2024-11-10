package fi.fabianadrian.proxychat.common.locale;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.translatable;

public interface Messages {
	Component GENERAL_ME = translatable("proxychat.general.me");
	Component GENERAL_UNKNOWN = translatable("proxychat.general.unknown");
}
