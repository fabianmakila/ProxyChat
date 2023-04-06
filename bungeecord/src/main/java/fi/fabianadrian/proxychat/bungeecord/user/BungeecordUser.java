package fi.fabianadrian.proxychat.bungeecord.user;

import fi.fabianadrian.proxychat.common.user.User;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BungeecordUser extends User<ProxiedPlayer> {
    private final Audience audience;
    public BungeecordUser(ProxiedPlayer player, Audience audience) {
        super(player);
        this.audience = audience;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public String name() {
        return this.player.getName();
    }

    @Override
    public @NotNull Audience audience() {
        return this.audience;
    }
}
