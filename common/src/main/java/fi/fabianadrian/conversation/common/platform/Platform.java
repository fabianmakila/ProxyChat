package fi.fabianadrian.conversation.common.platform;

import org.slf4j.Logger;

import java.nio.file.Path;

public interface Platform {
	Logger logger();

	Path dataDirectory();
}
