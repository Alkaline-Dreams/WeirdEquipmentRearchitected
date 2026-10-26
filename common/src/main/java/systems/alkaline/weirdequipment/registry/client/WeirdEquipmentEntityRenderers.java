package systems.alkaline.weirdequipment.registry.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import systems.alkaline.weirdequipment.entity.renderer.TorchArrowEntityRenderer;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentEntityTypes;

public class WeirdEquipmentEntityRenderers {
    public static void register() {
        EntityRendererRegistry.register(WeirdEquipmentEntityTypes.ROPE_COIL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(WeirdEquipmentEntityTypes.BLOCK_CANNON_SHOT, ctx -> new ThrownItemRenderer<>(ctx, 2.25f, false));
        EntityRendererRegistry.register(WeirdEquipmentEntityTypes.TORCH_ARROW, TorchArrowEntityRenderer::new);
    }
}
