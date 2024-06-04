package fi.fabianadrian.backend.common.config;

import space.arim.dazzleconf.annote.ConfDefault;

public interface BackendConfig {
	@ConfDefault.DefaultString("<<name>>: <message>")
	String format();
}
