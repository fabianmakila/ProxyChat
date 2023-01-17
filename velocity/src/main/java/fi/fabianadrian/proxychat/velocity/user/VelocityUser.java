package fi.fabianadrian.proxychat.velocity.user;

import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VelocityUser extends User<Player> {
    public VelocityUser(Player player) {
        super(player);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.base.hasPermission(permission);
    }

    @Override
    public UUID uuid() {
        return this.base.getUniqueId();
    }

    @Override
    public String name() {
        return this.base.getUsername();
    }

    @Override
    public @NotNull Audience audience() {
        return this.base;
    }
}
