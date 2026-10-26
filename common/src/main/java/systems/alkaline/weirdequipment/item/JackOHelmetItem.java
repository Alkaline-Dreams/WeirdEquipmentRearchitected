package xyz.venividivivi.weirdequipment.item;

import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/** A pumpkin helmet that glows. The light itself comes from LambDynamicLights. */
public class JackOHelmetItem extends Item {
    public JackOHelmetItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (!Platform.isModLoaded("lambdynlights")) {
            builder.accept(Component.translatable("item.weird_equipment.jack_o_helmet.dynamic_lights_warning"));
        }
    }
}
