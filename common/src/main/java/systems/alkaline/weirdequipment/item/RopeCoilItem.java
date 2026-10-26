package systems.alkaline.weirdequipment.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import systems.alkaline.weirdequipment.entity.RopeCoilEntity;

/** A throwable coil holding {@link #count} ropes. Thrown like a snowball. */
public class RopeCoilItem extends Item {
    public final int count;

    public RopeCoilItem(Properties properties, int count) {
        super(properties);
        this.count = count;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,
                0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(RopeCoilEntity::new, serverLevel, itemStack, player, 0.0f, 1.5f, 1.0f);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
