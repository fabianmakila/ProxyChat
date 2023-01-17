package fi.fabianadrian.proxychat.common.user;

import com.google.gson.Gson;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.platform.PlatformPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UserManager {

    private final Gson gson = new Gson();
    private final ProxyChat proxyChat;
    private final Path userDataDirectory;
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager(final ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.userDataDirectory = proxyChat.platform().dataDirectory().resolve("data/users");
    }

    public void loadUser(PlatformPlayer player) {
        Path file = this.userFile(player.uuid());
        User user = new User(player);
        if (Files.exists(file)) {
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                User deserialized = this.gson.fromJson(reader, User.class);
                user.populate(deserialized);
            } catch (Exception e) {
                this.proxyChat.platform().logger().warn("Failed to load data for user with UUID: " + player.uuid(), e);
            }
        }

        userMap.put(player.uuid(), user);
    }

    private void saveUser(final User user) {
        Path file = this.userFile(user.base().uuid());
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            this.gson.toJson(user, writer);
        } catch (Exception e) {
            this.proxyChat.platform().logger().warn("Failed to save data for user with UUID: " + user.base().uuid(), e);
        }
    }

    private Path userFile(UUID uuid) {
        return this.userDataDirectory.resolve(uuid + ".json");
    }

    public User user(UUID uuid) {
        User user = userMap.get(uuid);
        if (user == null) {
            throw new IllegalStateException("No user loaded for UUID: " + uuid);
        }

        return user;
    }

    public User user(PlatformPlayer player) {
        return user(player.uuid());
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
