package systems.alkaline.weirdequipment.client;

import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentItems;
import systems.alkaline.weirdequipment.registry.client.WeirdEquipmentEntityRenderers;

/** Client-only setup. Only call this on the client, so dedicated servers never load client classes. */
public final class WeirdEquipmentClient {
    public static void init() {
        WeirdEquipmentEntityRenderers.register();

        // The Jack o'Helmet's light comes from LambDynamicLights, so warn when it isn't installed.
        if (!Platform.isModLoaded("lambdynlights")) {
            ClientTooltipEvent.ITEM.register((stack, lines, context, flag) -> {
                if (stack.is(WeirdEquipmentItems.JACK_O_HELMET.get())) {
                    lines.add(Component.translatable("item.weird_equipment.jack_o_helmet.dynamic_lights_warning"));
                }
            });
        }
    }

    private WeirdEquipmentClient() {
    }
}
