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

	@ConfDefault.DefaultBoolean(true)
	boolean metrics();

	@SubSection
	interface FormatSection {
		@ConfDefault.DefaultString("<gold>[<red><sender> -> <receiver></red>]</gold> <message>")
		String msg();

		@ConfDefault.DefaultString("<gold>[<aqua><sender> -> <receiver></aqua>]</gold> <gray><message>")
		String msgSpy();

		@ConfDefault.DefaultString("<white>[<green>Broadcast</green>]</white> <message>")
		String broadcast();
	}
}
