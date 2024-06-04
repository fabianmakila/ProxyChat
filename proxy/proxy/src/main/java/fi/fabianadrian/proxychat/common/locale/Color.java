package fi.fabianadrian.proxychat.common.locale;

import net.kyori.adventure.text.format.TextColor;

public enum Color {
	PRIMARY("#34d399");

	public final TextColor textColor;

	Color(String hexString) {
		this.textColor = TextColor.fromHexString(hexString);
	}
}
