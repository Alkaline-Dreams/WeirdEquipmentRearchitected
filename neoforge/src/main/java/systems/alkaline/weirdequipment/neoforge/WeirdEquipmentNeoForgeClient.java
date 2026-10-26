package xyz.venividivivi.weirdequipment.neoforge;

import dev.architectury.platform.Platform;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import xyz.venividivivi.weirdequipment.config.WeirdEquipmentConfigScreen;

/** Client-only setup. Kept separate so dedicated servers never load client classes. */
final class WeirdEquipmentNeoForgeClient {
    static void init(ModContainer container) {
        if (Platform.isModLoaded("cloth_config")) {
            container.registerExtensionPoint(IConfigScreenFactory.class,
                    (IConfigScreenFactory) (mod, parent) -> WeirdEquipmentConfigScreen.create(parent));
        }
    }

    private WeirdEquipmentNeoForgeClient() {
    }
}
