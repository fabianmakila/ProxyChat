package fi.fabianadrian.proxychat.common.platform;

import cloud.commandframework.CommandManager;
import fi.fabianadrian.proxychat.common.command.Commander;
import org.slf4j.Logger;

import java.nio.file.Path;

public interface Platform {
    Logger logger();

    Path dataDirectory();

    CommandManager<Commander> commandManager();
}
