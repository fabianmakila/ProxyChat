package fi.fabianadrian.proxychat.common.hook.vanish;

import java.util.List;
import java.util.UUID;

public interface VanishHook {
    List<UUID> vanished();

    static VanishHook empty() {
        return List::of;
    }
}
