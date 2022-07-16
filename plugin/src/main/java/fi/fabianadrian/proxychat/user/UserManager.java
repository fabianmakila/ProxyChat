package fi.fabianadrian.proxychat.user;

import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.ProxyChat;
import fi.fabianadrian.proxychat.config.loader.UserConfigLoader;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UserManager {

    private final ProxyChat proxyChat;
    private final Path userDataDirectory;
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager(final ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.userDataDirectory = proxyChat.dataDirectory().resolve("data/users");
    }

    public void loadUser(Player player) {
        UserConfigLoader loader = loader(player.getUniqueId());
        try {
            User user = loader.load();
            user.player(player);
            this.userMap.put(player.getUniqueId(), user);
        } catch (ConfigurateException e) {
            this.proxyChat.logger().warn("Failed to load data for user with UUID: " + player.getUniqueId(), e);
        }
    }

    private UserConfigLoader loader(UUID uuid) {
        //ChannelSerializer channelSerializer = new ChannelSerializer(this.plugin.channelRegistry());
        return new UserConfigLoader(
                userDataDirectory.resolve(uuid + ".json")
        );
    }

    private void saveUser(final User user) {
        UserConfigLoader loader = this.loader(user.player().getUniqueId());
        try {
            loader.save(user);
        } catch (ConfigurateException e) {
            this.proxyChat.logger().warn("Failed to save data for user with UUID: " + user.player().getUniqueId(), e);
        }
    }

    public User user(UUID uuid) {
        User user = userMap.get(uuid);
        if (user == null) {
            throw new IllegalStateException("No user loaded for UUID: " + uuid);
        }

        return user;
    }

    public User user(Player player) {
        return user(player.getUniqueId());
    }

    public void unloadUser(final UUID uuid) {
        final User removed = this.userMap.remove(uuid);
        if (removed == null) {
            throw new IllegalStateException("Cannot remove non-existing user " + uuid);
        }
        this.saveUser(removed);
    }

    public Collection<User> users() {
        return this.userMap.values();
    }
}
