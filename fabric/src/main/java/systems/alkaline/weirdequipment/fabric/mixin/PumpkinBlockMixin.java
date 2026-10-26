package xyz.venividivivi.weirdequipment.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentModTags;

/** Lets any shears carve pumpkins, not just vanilla's. (NeoForge already does this.) */
@Mixin(PumpkinBlock.class)
abstract class PumpkinBlockMixin {
    // ItemStack.is(Item) is the generic TypedInstance.is(T), so it compiles to is(Object).
    @WrapOperation(method = "useItemOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private boolean weirdEquipment$isShears(ItemStack stack, Object item, Operation<Boolean> original) {
        return original.call(stack, item) || stack.is(WeirdEquipmentModTags.SHEARS);
    }
}
