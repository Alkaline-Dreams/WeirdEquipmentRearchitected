package xyz.venividivivi.weirdequipment.entity.renderer;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import xyz.venividivivi.weirdequipment.WeirdEquipment;
import xyz.venividivivi.weirdequipment.entity.TorchArrowEntity;

public class TorchArrowEntityRenderer extends ArrowRenderer<TorchArrowEntity, ArrowRenderState> {
    private static final Identifier TEXTURE = WeirdEquipment.id("textures/entity/projectiles/torch_arrow.png");

    public TorchArrowEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }
}
