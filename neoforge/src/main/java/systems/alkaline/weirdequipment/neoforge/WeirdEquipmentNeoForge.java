package xyz.venividivivi.weirdequipment.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import xyz.venividivivi.weirdequipment.WeirdEquipment;

@Mod(WeirdEquipment.MOD_ID)
public final class WeirdEquipmentNeoForge {
    public WeirdEquipmentNeoForge(ModContainer container) {
        WeirdEquipment.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            WeirdEquipmentNeoForgeClient.init(container);
        }
    }
}
