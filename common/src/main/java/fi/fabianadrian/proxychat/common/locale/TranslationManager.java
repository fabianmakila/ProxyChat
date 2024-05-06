package fi.fabianadrian.proxychat.common.locale;

import fi.fabianadrian.proxychat.common.ProxyChat;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class TranslationManager {
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	public static final List<Locale> BUNDLED_LOCALES = List.of(new Locale("fi", "FI"));

	private final ResourceBundle defaultBundle = ResourceBundle.getBundle("messages", DEFAULT_LOCALE, UTF8ResourceBundleControl.get());
	private final Path translationsDirectory;
	private final Logger logger;
	private TranslationRegistry registry;

	public TranslationManager(ProxyChat proxyChat) {
		this.logger = proxyChat.platform().logger();
		this.translationsDirectory = proxyChat.platform().dataDirectory().resolve("translations");
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private static boolean isAdventureDuplicatesException(Exception e) {
		return e instanceof IllegalArgumentException && (e.getMessage().startsWith("Invalid key") || e.getMessage().startsWith("Translation already exists"));
	}

	public void reload() {
		if (this.registry != null) {
			GlobalTranslator.translator().removeSource(this.registry);
		}

		this.registry = TranslationRegistry.create(Key.key("proxychat", "main"));
		this.registry.defaultLocale(DEFAULT_LOCALE);

		// load custom translations first, then the base (built-in) translations after.
		loadFromFileSystem(this.translationsDirectory);
		loadFromResourceBundle();

		// register it to the global source, so our translations can be picked up by adventure-platform
		GlobalTranslator.translator().addSource(this.registry);
	}

	/**
	 * Loads the bundled translations included inside the jar.
	 */
	private void loadFromResourceBundle() {
		try {
			// Register default bundle first
			this.registry.registerAll(DEFAULT_LOCALE, defaultBundle, false);

			// Then the rest of the included bundles
			BUNDLED_LOCALES.forEach(locale -> {
				ResourceBundle bundle = ResourceBundle.getBundle("messages", locale, UTF8ResourceBundleControl.get());
				this.registry.registerAll(locale, bundle, false);
			});
		} catch (IllegalArgumentException e) {
			this.logger.warn("Error loading default locale file", e);
		}
	}

	/**
	 * Creates the translation directory by writing the example translation in there.
	 */
	private void writeExampleTranslationsToDisk() {
		Properties properties = new Properties();
		// Extract all key-value pairs from the ResourceBundle
		Enumeration<String> keys = this.defaultBundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = this.defaultBundle.getString(key);
			properties.setProperty(key, value);
		}

		try (OutputStream outputStream = new FileOutputStream(this.translationsDirectory.resolve("messages_example.properties").toFile())) {
			Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			properties.store(writer, null);
		} catch (IOException e) {
			this.logger.warn("Error saving example translation file", e);
		}
	}

	private void loadFromFileSystem(Path directory) {
		writeExampleTranslationsToDisk();

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
					if (!isAdventureDuplicatesException(e)) {
						this.logger.warn("Error loading locale file: {}", translationFile.getFileName(), e);
					}
				}
			}

			this.logger.info("Loaded custom translations for {}", loadedLocaleNamesJoiner);
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
