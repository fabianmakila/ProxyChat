package fi.fabianadrian.proxychat.velocity;

import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.common.user.PlatformPlayer;

import java.util.UUID;

public class VelocityPlatformPlayer implements PlatformPlayer {

    private final Player player;

    public VelocityPlatformPlayer(Player player) {
        this.player = player;
    }

    @Override
    public UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public String name() {
        return this.player.getUsername();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }
}
