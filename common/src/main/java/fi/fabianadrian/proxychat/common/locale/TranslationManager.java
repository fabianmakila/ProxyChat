package fi.fabianadrian.proxychat.common.locale;

import fi.fabianadrian.proxychat.common.ProxyChat;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class TranslationManager {
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	public static final List<Locale> BUNDLED_LOCALES = List.of(DEFAULT_LOCALE, new Locale("fi", "FI"));

	private final Path translationsDirectory;
	private final Logger logger;
	private TranslationRegistry registry;

	public TranslationManager(ProxyChat proxyChat) {
		this.logger = proxyChat.platform().logger();
		this.translationsDirectory = proxyChat.platform().dataDirectory().resolve("translations");
	}

	private static boolean isAdventureDuplicatesException(Exception e) {
		return e instanceof IllegalArgumentException && (e.getMessage().startsWith("Invalid key") || e.getMessage().startsWith("Translation already exists"));
	}

	public void reload() {
		if (this.registry != null) {
			GlobalTranslator.translator().removeSource(this.registry);
		}

		this.registry = TranslationRegistry.create(Key.key("proxychat", "main"));
		this.registry.defaultLocale(DEFAULT_LOCALE);

		try {
			Files.createDirectories(this.translationsDirectory);

			writeExampleTranslationsToDisk();
			loadFromFileSystem(this.translationsDirectory);
		} catch (IOException e) {
			this.logger.error("Could not create translations directory", e);
		}

		loadFromResourceBundle();

		// register to the global source, so our translations can be picked up by adventure-platform
		GlobalTranslator.translator().addSource(this.registry);
	}

	/**
	 * Loads the bundled translations included inside the jar.
	 */
	private void loadFromResourceBundle() {
		BUNDLED_LOCALES.forEach(locale -> {
			ResourceBundle bundle = ResourceBundle.getBundle("messages", locale, UTF8ResourceBundleControl.get());
			try {
				this.registry.registerAll(locale, bundle, false);
			} catch (IllegalArgumentException e) {
				if (isAdventureDuplicatesException(e)) {
					return;
				}
				this.logger.warn("Error loading default locale file", e);
			}
		});
	}

	private void writeExampleTranslationsToDisk() {
		ResourceBundle defaultBundle = ResourceBundle.getBundle("messages", DEFAULT_LOCALE, UTF8ResourceBundleControl.get());

		// Extract all key-value pairs from the ResourceBundle
		List<String> lines = new ArrayList<>();
		defaultBundle.getKeys().asIterator().forEachRemaining(key -> lines.add(key + "=" + defaultBundle.getString(key)));
		Collections.sort(lines);

		try {
			Files.write(this.translationsDirectory.resolve("messages_example.properties"), lines);
		} catch (IOException e) {
			this.logger.warn("Error saving example translation file", e);
		}
	}

	private void loadFromFileSystem(Path directory) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.properties")) {
			StringJoiner loadedLocaleNamesJoiner = new StringJoiner(", ");

			for (Path translationFile : stream) {
				// Skip reference file
				if ("messages_example.properties".equalsIgnoreCase(translationFile.getFileName().toString())) {
					continue;
				}

				try {
					Locale locale = loadTranslationFile(translationFile);
					loadedLocaleNamesJoiner.add(locale.getLanguage());
				} catch (Exception e) {
					this.logger.warn("Error loading locale file: {}", translationFile.getFileName(), e);
				}
			}

			if (loadedLocaleNamesJoiner.length() != 0) {
				this.logger.info("Loaded custom translations for {}", loadedLocaleNamesJoiner);
			}
		} catch (IOException e) {
			this.logger.warn("Error reading the translations directory", e);
		}
	}

	private Locale loadTranslationFile(Path translationFile) throws IOException {
		String fileName = translationFile.getFileName().toString();
		String localeString = fileName.substring(
				"messages_".length(),
				fileName.length() - ".properties".length()
		);
		Locale locale = Translator.parseLocale(localeString);
		if (locale == null) {
			throw new IllegalStateException("Unknown locale '" + localeString + "' - unable to register.");
		}

		PropertyResourceBundle bundle;
		try (BufferedReader reader = Files.newBufferedReader(translationFile, StandardCharsets.UTF_8)) {
			bundle = new PropertyResourceBundle(reader);
		}

		this.registry.registerAll(locale, bundle, false);
		return locale;
	}
}
