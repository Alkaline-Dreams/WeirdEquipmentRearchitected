package systems.alkaline.weirdequipment.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;
import systems.alkaline.weirdequipment.config.WeirdEquipmentConfig;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentEntityTypes;

/** A torch fired from the Torch Bow. Places a torch where it lands, or sets mobs on fire. */
public class TorchArrowEntity extends AbstractArrow {
    private static final int FIRE_SECONDS = 3;
    private static final int FLAME_FIRE_SECONDS = 6;

    public TorchArrowEntity(EntityType<? extends TorchArrowEntity> type, Level level) {
        super(type, level);
    }

    public TorchArrowEntity(Level level, LivingEntity owner, ItemStack pickupItem, @Nullable ItemStack weapon) {
        super(WeirdEquipmentEntityTypes.TORCH_ARROW.get(), owner, level, pickupItem, weapon);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (this.level() instanceof ServerLevel level) {
            Direction side = hitResult.getDirection();
            BlockPos pos = hitResult.getBlockPos().relative(side);
            BlockState torch = side.getAxis().isHorizontal()
                    ? Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, side)
                    : Blocks.TORCH.defaultBlockState();

            if (level.getBlockState(pos).isAir() && torch.canSurvive(level, pos)) {
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 2.25f, 0.9f);
                level.setBlockAndUpdate(pos, torch);
                level.gameEvent(this.getOwner(), GameEvent.BLOCK_PLACE, pos);
            } else {
                level.playSound(null, pos, SoundEvents.ARROW_HIT, SoundSource.BLOCKS, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
                this.dropTorch(level);
            }
        }
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if (entity instanceof LivingEntity && WeirdEquipmentConfig.TORCH_BOW_CAN_DAMAGE_MOBS) {
            // Flame makes the arrow itself burn; that sets mobs on fire for longer.
            int fireSeconds = this.isOnFire() ? FLAME_FIRE_SECONDS : FIRE_SECONDS;
            // Vanilla arrow damage, so Power, Punch and crits still apply.
            super.onHitEntity(hitResult);
            entity.igniteForSeconds(fireSeconds);
        } else if (this.level() instanceof ServerLevel level) {
            this.dropTorch(level);
        }
        this.discard();
    }

    private void dropTorch(ServerLevel level) {
        if (this.pickup == Pickup.ALLOWED) {
            this.spawnAtLocation(level, new ItemStack(Items.TORCH));
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.TORCH);
    }
}
