package xyz.venividivivi.weirdequipment;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.resources.Identifier;
import xyz.venividivivi.weirdequipment.config.WeirdEquipmentConfig;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentBlocks;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentEntityTypes;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentItems;
import xyz.venividivivi.weirdequipment.registry.client.WeirdEquipmentEntityRenderers;

public class WeirdEquipment {
    public static final String MOD_ID = "weird_equipment";

    public static void init() {
        WeirdEquipmentConfig.init();
        WeirdEquipmentBlocks.register();
        WeirdEquipmentItems.register();
        WeirdEquipmentEntityTypes.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            WeirdEquipmentEntityRenderers.register();
        }
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
