package systems.alkaline.weirdequipment.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.architectury.platform.Platform;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;

public class WeirdEquipmentConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("Weird Equipment Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final boolean DEFAULT_TORCH_BOW_CAN_DAMAGE_MOBS = true;
    public static final int DEFAULT_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = 5;
    public static final int MIN_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = 0;
    public static final int MAX_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = 10;
    public static final float DEFAULT_SELF_SLINGSHOT_VELOCITY = 1;
    public static final float MIN_SELF_SLINGSHOT_VELOCITY = 0;
    public static final float MAX_SELF_SLINGSHOT_VELOCITY = 5;

    public static boolean TORCH_BOW_CAN_DAMAGE_MOBS = DEFAULT_TORCH_BOW_CAN_DAMAGE_MOBS;
    public static int NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = DEFAULT_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS;
    public static float SELF_SLINGSHOT_VELOCITY = DEFAULT_SELF_SLINGSHOT_VELOCITY;

    private static Path file() {
        return Platform.getConfigFolder().resolve("weird_equipment.json");
    }

    public static void init() {
        load();
    }

    public static void load() {
        Path file = file();
        if (!Files.exists(file)) {
            LOGGER.info("Could not find config, creating one with defaults");
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            fromJson(JsonParser.parseReader(reader).getAsJsonObject());
        } catch (Exception e) {
            // Leave the file alone so one typo doesn't wipe the user's settings.
            LOGGER.error("Could not read config '{}', using defaults until it's fixed", file, e);
            return;
        }
        // Adds entries that are missing and replaces ones that were invalid.
        save();
    }

    public static void save() {
        Path file = file();
        try (Writer writer = Files.newBufferedWriter(file)) {
            writer.write(GSON.toJson(toJson()));
        } catch (IOException e) {
            LOGGER.error("Failed to save config to '{}'", file, e);
        }
    }

    /** Missing or invalid entries fall back to their defaults; out-of-range numbers are clamped. */
    public static void fromJson(JsonObject json) {
        TORCH_BOW_CAN_DAMAGE_MOBS = read(json, "torch_bow_can_damage_mobs",
                DEFAULT_TORCH_BOW_CAN_DAMAGE_MOBS, GsonHelper::convertToBoolean);
        NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = Mth.clamp(read(json, "netherite_torch_pickaxe_durability_loss",
                        DEFAULT_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS, GsonHelper::convertToInt),
                MIN_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS, MAX_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS);
        float velocity = read(json, "self_slingshot_velocity", DEFAULT_SELF_SLINGSHOT_VELOCITY, GsonHelper::convertToFloat);
        SELF_SLINGSHOT_VELOCITY = Float.isFinite(velocity)
                ? Mth.clamp(velocity, MIN_SELF_SLINGSHOT_VELOCITY, MAX_SELF_SLINGSHOT_VELOCITY)
                : DEFAULT_SELF_SLINGSHOT_VELOCITY;
    }

    private static <T> T read(JsonObject json, String key, T fallback, BiFunction<JsonElement, String, T> convert) {
        if (!json.has(key)) return fallback;
        try {
            return convert.apply(json.get(key), key);
        } catch (RuntimeException e) {
            LOGGER.warn("Invalid value for '{}' in config, using default ({})", key, fallback);
            return fallback;
        }
    }

    public static JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("torch_bow_can_damage_mobs", TORCH_BOW_CAN_DAMAGE_MOBS);
        json.addProperty("netherite_torch_pickaxe_durability_loss", NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS);
        json.addProperty("self_slingshot_velocity", SELF_SLINGSHOT_VELOCITY);
        return json;
    }
}
