package systems.alkaline.weirdequipment.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

/** A bucket that never empties. */
public class BottomlessBucketItem extends BucketItem {
    public BottomlessBucketItem(Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // super.use() uses up the held stack when it pours, so keep a copy.
        ItemStack bucket = player.getItemInHand(hand).copy();
        InteractionResult result = super.use(level, player, hand);
        if (result instanceof InteractionResult.Success success && success.heldItemTransformedTo() != null) {
            return success.heldItemTransformedTo(bucket);
        }
        return result;
    }
}
