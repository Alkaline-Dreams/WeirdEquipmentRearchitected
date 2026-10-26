package xyz.venividivivi.weirdequipment.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.architectury.platform.Platform;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class WeirdEquipmentConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("Weird Equipment Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final boolean DEFAULT_TORCH_BOW_CAN_DAMAGE_MOBS = true;
    private static final int DEFAULT_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = 5;
    private static final float DEFAULT_SELF_SLINGSHOT_VELOCITY = 1;

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
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                fromJson(JsonParser.parseReader(reader).getAsJsonObject());
            } catch (Exception e) {
                LOGGER.error("Could not load config from '{}', using defaults", file, e);
            }
        } else {
            LOGGER.info("Could not find config, creating one with defaults");
        }
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

    // Missing or invalid entries fall back to their defaults.
    public static void fromJson(JsonObject json) {
        TORCH_BOW_CAN_DAMAGE_MOBS = GsonHelper.getAsBoolean(json, "torch_bow_can_damage_mobs", DEFAULT_TORCH_BOW_CAN_DAMAGE_MOBS);
        NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = GsonHelper.getAsInt(json, "netherite_torch_pickaxe_durability_loss", DEFAULT_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS);
        SELF_SLINGSHOT_VELOCITY = GsonHelper.getAsFloat(json, "self_slingshot_velocity", DEFAULT_SELF_SLINGSHOT_VELOCITY);
    }

    public static JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("torch_bow_can_damage_mobs", TORCH_BOW_CAN_DAMAGE_MOBS);
        json.addProperty("netherite_torch_pickaxe_durability_loss", NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS);
        json.addProperty("self_slingshot_velocity", SELF_SLINGSHOT_VELOCITY);
        return json;
    }
}
