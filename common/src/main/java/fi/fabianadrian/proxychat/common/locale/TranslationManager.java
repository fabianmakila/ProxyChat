package fi.fabianadrian.proxychat.common.locale;

import com.google.common.collect.Maps;
import fi.fabianadrian.proxychat.common.ProxyChat;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TranslationManager {
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    public static final List<Locale> BUNDLED_LOCALES = List.of(new Locale("fi", "FI"));

    private final Set<Locale> installed = ConcurrentHashMap.newKeySet();
    private final Path translationsDirectory;
    private final Logger logger;
    private TranslationRegistry registry;

    public TranslationManager(ProxyChat proxyChat) {
        this.logger = proxyChat.platform().logger();
        this.translationsDirectory = proxyChat.platform().dataDirectory().resolve("translations");

        createDirectoryIfNotExists(this.translationsDirectory);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isAdventureDuplicatesException(Exception e) {
        return e instanceof IllegalArgumentException && (e.getMessage().startsWith("Invalid key") || e.getMessage().startsWith("Translation already exists"));
    }

    public static Locale parseLocale(String locale) {
        return locale == null ? null : Translator.parseLocale(locale);
    }

    public void reload() {
        // remove any previous registry
        if (this.registry != null) {
            GlobalTranslator.translator().removeSource(this.registry);
        }

        // create a translation registry
        this.registry = TranslationRegistry.create(Key.key("proxychat", "main"));
        this.registry.defaultLocale(DEFAULT_LOCALE);

        // load custom translations first, then the base (built-in) translations after.
        loadFromFileSystem(this.translationsDirectory, false);
        loadFromResourceBundle();

        // register it to the global source, so our translations can be picked up by adventure-platform
        GlobalTranslator.translator().addSource(this.registry);
    }

    /**
     * Loads the bundled translations from the jar file.
     */
    private void loadFromResourceBundle() {
        ResourceBundle defaultBundle = ResourceBundle.getBundle("messages", DEFAULT_LOCALE);
        try {
            this.registry.registerAll(DEFAULT_LOCALE, defaultBundle, false);
            BUNDLED_LOCALES.forEach(locale -> {
                ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
                this.registry.registerAll(locale, bundle, false);
            });
        } catch (IllegalArgumentException e) {
            this.logger.warn("Error loading default locale file", e);
        }
    }

    public void loadFromFileSystem(Path directory, boolean suppressDuplicatesError) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.properties")) {
            Map<Locale, ResourceBundle> loaded = new HashMap<>();

            for (Path translationFile : stream) {
                try {
                    Map.Entry<Locale, ResourceBundle> result = loadTranslationFile(translationFile);
                    loaded.put(result.getKey(), result.getValue());
                } catch (Exception e) {
                    if (!suppressDuplicatesError || !isAdventureDuplicatesException(e)) {
                        this.logger.warn("Error loading locale file: " + translationFile.getFileName(), e);
                    }
                }
            }

            // try registering the locale without a country code - if we don't already have a registration for that
            loaded.forEach((locale, bundle) -> {
                Locale localeWithoutCountry = new Locale(locale.getLanguage());
                if (!locale.equals(localeWithoutCountry) && !localeWithoutCountry.equals(DEFAULT_LOCALE) && this.installed.add(localeWithoutCountry)) {
                    try {
                        this.registry.registerAll(localeWithoutCountry, bundle, false);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            });
        } catch (IOException e) {
            this.logger.warn("Error reading the translations directory", e);
        }
    }

    private Map.Entry<Locale, ResourceBundle> loadTranslationFile(Path translationFile) throws IOException {
        String fileName = translationFile.getFileName().toString();
        String localeString = fileName.substring(0, fileName.length() - ".properties".length());
        Locale locale = parseLocale(localeString);

        if (locale == null) {
            throw new IllegalStateException("Unknown locale '" + localeString + "' - unable to register.");
        }

        PropertyResourceBundle bundle;
        try (BufferedReader reader = Files.newBufferedReader(translationFile, StandardCharsets.UTF_8)) {
            bundle = new PropertyResourceBundle(reader);
        }

        this.registry.registerAll(locale, bundle, false);
        this.installed.add(locale);
        return Maps.immutableEntry(locale, bundle);
    }

    private void createDirectoryIfNotExists(Path path) {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return;
        }

        try {
            Files.createDirectory(path);
        } catch (IOException ignored) {
        }

    }
}
