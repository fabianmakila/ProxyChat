package fi.fabianadrian.proxychat.common.config;

import space.arim.dazzleconf.annote.ConfDefault;

import java.util.List;

public interface AnnouncementsConfig {
	@ConfDefault.DefaultStrings({})
	List<String> announcements();

	@ConfDefault.DefaultInteger(30)
	int interval();

	@ConfDefault.DefaultBoolean(false)
	boolean random();

	@ConfDefault.DefaultString("<gray>[<green>!</green>]</gray> ")
	String prefix();
}
