package fi.fabianadrian.proxychat.common.platform;

import cloud.commandframework.CommandManager;
import fi.fabianadrian.proxychat.common.command.Commander;
import fi.fabianadrian.proxychat.common.hook.HookManager;
import net.kyori.adventure.audience.ForwardingAudience;
import org.slf4j.Logger;

import java.nio.file.Path;

public interface Platform extends ForwardingAudience.Single {
    Logger logger();

    Path dataDirectory();

    CommandManager<Commander> commandManager();

    HookManager hookManager();
}
