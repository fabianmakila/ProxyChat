package fi.fabianadrian.proxychat.common.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.List;

public interface ProxyChatConfig {
	FormatSection formats();

	@ConfComments("Placeholders: <name>")
	@ConfDefault.DefaultStrings({
			"<rainbow>----------------------------------------</rainbow>",
			"        Welcome to our network, <name>!",
			"<rainbow>----------------------------------------</rainbow>"
	})
	List<String> welcomeMessage();

	@ConfDefault.DefaultBoolean(false)
	boolean globalChat();

	@SubSection
	interface FormatSection {
		@ConfDefault.DefaultString("<gold>[<green>Broadcast</green>]</gold> <message>")
		String broadcast();

		@ConfDefault.DefaultString("<gold>[<green>Global</green>]</gold> <sender>: <message>")
		String global();

		@ConfDefault.DefaultString("<gold>[<red><sender> -> <receiver></red>]</gold> <message>")
		String msg();

		@ConfDefault.DefaultString("<gold>[<aqua><sender> -> <receiver></aqua>]</gold> <gray><message>")
		String msgSpy();
	}
}
