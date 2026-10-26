package systems.alkaline.weirdequipment.fabric.addon;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.architectury.platform.Platform;
import systems.alkaline.weirdequipment.config.WeirdEquipmentConfigScreen;

public class ModMenuAddon implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (Platform.isModLoaded("cloth-config")) {
            return WeirdEquipmentConfigScreen::create;
        }
        return parent -> null;
    }
}
