package fi.fabianadrian.proxychat.config.loader;

import fi.fabianadrian.proxychat.user.User;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.file.Path;

public class UserConfigLoader {

    private final GsonConfigurationLoader loader;
    private final ObjectMapper<User> mapper;

    public UserConfigLoader(Path configPath) {
        this.loader = GsonConfigurationLoader.builder()
                .path(configPath)
                .build();
        try {
            this.mapper = ObjectMapper.factory().get(User.class);
        } catch (SerializationException e) {
            throw new IllegalStateException(
                    "Failed to initialize an object mapper for type: " + User.class.getSimpleName(),
                    e
            );
        }
    }

    public User load() throws ConfigurateException {
        BasicConfigurationNode node = this.loader.load();
        return this.mapper.load(node);
    }

    public void save(User config) throws ConfigurateException {
        BasicConfigurationNode node = this.loader.createNode();
        this.mapper.save(config, node);
        this.loader.save(node);
    }
}
