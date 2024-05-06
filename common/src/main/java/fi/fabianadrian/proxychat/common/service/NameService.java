package fi.fabianadrian.proxychat.common.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fi.fabianadrian.proxychat.common.ProxyChat;
import fi.fabianadrian.proxychat.common.user.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class NameService {
	private final Gson gson = new Gson();
	private final ProxyChat proxyChat;
	private final Path databasePath;
	private Map<UUID, String> nameMap = new HashMap<>();

	public NameService(ProxyChat proxyChat) {
		this.proxyChat = proxyChat;
		this.databasePath = proxyChat.platform().dataDirectory().resolve("data/usernames.json");
	}

	public void reload() {
		try (BufferedReader reader = Files.newBufferedReader(this.databasePath)) {
			Map<UUID, String> mapFromJson = this.gson.fromJson(reader, new TypeToken<>() {});
			if (mapFromJson == null) {
				return;
			}

			this.nameMap = mapFromJson;
		} catch (Exception e) {
			this.proxyChat.platform().logger().warn("Failed to load usernames.json", e);
		}
	}

	public Optional<String> resolve(UUID uuid) {
		return Optional.of(this.nameMap.get(uuid));
	}

	public void update(User user) {
		this.nameMap.put(user.uuid(), user.name());
		save();
	}

	public void save() {
		try (BufferedWriter writer = Files.newBufferedWriter(this.databasePath)) {
			this.gson.toJson(this.nameMap, writer);
		} catch (Exception e) {
			this.proxyChat.platform().logger().warn("Failed to save usernames.json", e);
		}
	}
}
