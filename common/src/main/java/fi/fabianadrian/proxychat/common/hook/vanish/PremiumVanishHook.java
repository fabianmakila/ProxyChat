package fi.fabianadrian.proxychat.common.hook.vanish;

import de.myzelyam.api.vanish.VanishAPI;

import java.util.List;
import java.util.UUID;

public class PremiumVanishHook implements VanishHook {
    @Override
    public List<UUID> vanished() {
        return VanishAPI.getInvisiblePlayers();
    }
}
