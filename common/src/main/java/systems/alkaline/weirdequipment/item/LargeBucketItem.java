package xyz.venividivivi.weirdequipment.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

/**
 * A bucket holding several bucketfuls, one per point of durability. When the last one is
 * poured it turns back into the empty buckets it was crafted from.
 */
public class LargeBucketItem extends BucketItem {
    public LargeBucketItem(Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // super.use() uses up the held stack when it pours, so keep a copy.
        ItemStack bucket = player.getItemInHand(hand).copy();
        InteractionResult result = super.use(level, player, hand);
        if (!(result instanceof InteractionResult.Success success) || success.heldItemTransformedTo() == null) {
            return result;
        }
        if (player.hasInfiniteMaterials()) {
            return success.heldItemTransformedTo(bucket);
        }

        int buckets = bucket.getMaxDamage();
        bucket.hurtAndBreak(1, player, hand);
        return success.heldItemTransformedTo(bucket.isEmpty() ? new ItemStack(Items.BUCKET, buckets) : bucket);
    }
}
