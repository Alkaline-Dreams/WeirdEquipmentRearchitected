package xyz.venividivivi.weirdequipment.fabric;

import xyz.venividivivi.weirdequipment.WeirdEquipment;
import net.fabricmc.api.ModInitializer;

public class WeirdEquipmentFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WeirdEquipment.init();
    }
}
