package systems.alkaline.weirdequipment.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import systems.alkaline.weirdequipment.entity.BlockCannonShotEntity;

// Fires the block held in the off-hand; it's placed wherever it lands.
public class BlockCannonItem extends Item {
    private static final float SHOT_VELOCITY = 4.5f;

    public BlockCannonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack ammo = player.getOffhandItem();
        // Only fire past normal reach; up close the off-hand block is placed the usual way.
        if (hand != InteractionHand.MAIN_HAND || !(ammo.getItem() instanceof BlockItem)
                || player.pick(player.blockInteractionRange(), 0.0f, false).getType() == HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(
                    (lvl, owner, stack) -> new BlockCannonShotEntity(lvl, owner, stack.copyWithCount(1)),
                    serverLevel, ammo, player, 0.0f, SHOT_VELOCITY, 0.0f);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS,
                0.2f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + 0.5f);

        player.awardStat(Stats.ITEM_USED.get(this));
        player.getItemInHand(hand).hurtAndBreak(1, player, hand);
        ammo.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
