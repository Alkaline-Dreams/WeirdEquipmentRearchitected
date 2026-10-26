package systems.alkaline.weirdequipment;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.resources.Identifier;
import systems.alkaline.weirdequipment.config.WeirdEquipmentConfig;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentBlocks;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentEntityTypes;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentItems;
import systems.alkaline.weirdequipment.registry.client.WeirdEquipmentEntityRenderers;

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
