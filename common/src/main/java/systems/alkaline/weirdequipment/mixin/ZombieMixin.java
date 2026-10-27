package systems.alkaline.weirdequipment.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentItems;

/** Right-clicking an empty-handed zombie with a Dirt Sword hands the sword over. */
@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (this.getMainHandItem().isEmpty() && held.is(WeirdEquipmentItems.DIRT_SWORD.get())) {
            // Treated like a picked-up item: always dropped on death, and the zombie won't despawn.
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, held.copyWithCount(1));
            this.setPersistenceRequired();
            held.consume(1, player);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }
}
