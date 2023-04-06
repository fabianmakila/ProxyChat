package fi.fabianadrian.proxychat.common.user;

import com.google.gson.Gson;
import fi.fabianadrian.proxychat.common.ProxyChat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class UserManager {

    private final Gson gson = new Gson();
    private final ProxyChat proxyChat;
    private final Path userDataDirectory;
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager(final ProxyChat proxyChat) {
        this.proxyChat = proxyChat;
        this.userDataDirectory = proxyChat.platform().dataDirectory().resolve("data/users");
    }

    public User loadUser(PlatformPlayer player) {
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

        this.userMap.put(player.uuid(), user);
        return user;
    }

    private void saveUser(final User user) {
        Path file = this.userFile(user.uuid());
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            this.gson.toJson(user, writer);
        } catch (Exception e) {
            this.proxyChat.platform().logger().warn("Failed to save data for user with UUID: " + user.uuid(), e);
        }
    }

    private Path userFile(UUID uuid) {
        return this.userDataDirectory.resolve(uuid + ".json");
    }

    public Optional<User> user(UUID uuid) {
        User user = this.userMap.get(uuid);
        return Optional.ofNullable(user);
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
