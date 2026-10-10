package com.universalxp.nolungehunger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tiny config: config/nolungehunger.json -> { "enabled": true }.
 * The file is re-checked about once a second, so editing it takes effect
 * immediately (no game restart needed).
 */
public final class NoLungeHungerConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final long CHECK_INTERVAL_MS = 1000L;

	private static volatile boolean enabled = true;
	private static volatile long lastCheck = 0L;
	private static volatile long lastModified = -1L;

	private NoLungeHungerConfig() {
	}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("nolungehunger.json");
	}

	/** Creates the default file if missing and loads it. */
	public static void init() {
		Path path = file();
		if (!Files.exists(path)) {
			save();
		}
		load();
	}

	public static boolean isEnabled() {
		long now = System.currentTimeMillis();
		if (now - lastCheck >= CHECK_INTERVAL_MS) {
			lastCheck = now;
			try {
				Path path = file();
				if (Files.exists(path)) {
					long modified = Files.getLastModifiedTime(path).toMillis();
					if (modified != lastModified) {
						load();
					}
				}
			} catch (IOException ignored) {
				// keep the last known value
			}
		}
		return enabled;
	}

	private static void load() {
		Path path = file();
		try {
			String text = Files.readString(path, StandardCharsets.UTF_8);
			JsonObject json = GSON.fromJson(text, JsonObject.class);
			if (json != null && json.has("enabled")) {
				enabled = json.get("enabled").getAsBoolean();
			}
			lastModified = Files.getLastModifiedTime(path).toMillis();
		} catch (Exception e) {
			NoLungeHunger.LOGGER.warn("Could not read {}, keeping enabled={}", path, enabled);
		}
	}

	private static void save() {
		JsonObject json = new JsonObject();
		json.addProperty("enabled", enabled);
		try {
			Path path = file();
			Files.createDirectories(path.getParent());
			Files.writeString(path, GSON.toJson(json), StandardCharsets.UTF_8);
			lastModified = Files.getLastModifiedTime(path).toMillis();
		} catch (IOException e) {
			NoLungeHunger.LOGGER.warn("Could not write config", e);
		}
	}
}
