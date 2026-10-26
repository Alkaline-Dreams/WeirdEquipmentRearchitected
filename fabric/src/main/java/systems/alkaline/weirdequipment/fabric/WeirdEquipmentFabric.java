package systems.alkaline.weirdequipment.fabric;

import systems.alkaline.weirdequipment.WeirdEquipment;
import net.fabricmc.api.ModInitializer;

public class WeirdEquipmentFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WeirdEquipment.init();
    }
}
