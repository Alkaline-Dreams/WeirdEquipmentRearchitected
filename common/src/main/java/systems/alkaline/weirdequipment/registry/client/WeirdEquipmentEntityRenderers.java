package xyz.venividivivi.weirdequipment.registry.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import xyz.venividivivi.weirdequipment.entity.renderer.TorchArrowEntityRenderer;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentEntityTypes;

public class WeirdEquipmentEntityRenderers {
    public static void register() {
        EntityRendererRegistry.register(WeirdEquipmentEntityTypes.ROPE_COIL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(WeirdEquipmentEntityTypes.TORCH_ARROW, TorchArrowEntityRenderer::new);
    }
}
