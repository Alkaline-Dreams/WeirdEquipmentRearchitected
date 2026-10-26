package xyz.venividivivi.weirdequipment.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import xyz.venividivivi.weirdequipment.block.WallRopeBlock;
import xyz.venividivivi.weirdequipment.item.RopeCoilItem;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentBlocks;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentEntityTypes;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentItems;

/**
 * A thrown rope coil. When it hits a block it freezes in place and unrolls its ropes
 * downward one at a time. The thrown item is synced to clients by the parent class, which
 * picks the right sprite.
 */
public class RopeCoilEntity extends ThrowableItemProjectile {
    private static final int TICKS_PER_ROPE = 3;

    private boolean isPlacing = false;
    private BlockPos ropePos = null;
    private int ticksUntilNextRope = 0;
    private int ropesLeft = 0;

    public RopeCoilEntity(EntityType<? extends RopeCoilEntity> type, Level level) {
        super(type, level);
    }

    public RopeCoilEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(WeirdEquipmentEntityTypes.ROPE_COIL.get(), owner, level, stack);
        if (stack.getItem() instanceof RopeCoilItem coil) {
            this.ropesLeft = coil.count;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        // Once frozen in place, ignore further hits.
        if (isPlacing || !(this.level() instanceof ServerLevel level)) return;

        Direction side = hitResult.getDirection();
        BlockPos pos = hitResult.getBlockPos().relative(side);
        if (!level.getBlockState(pos).isAir() || side == Direction.UP) {
            dropCoil(level);
            return;
        }

        BlockState rope = side.getAxis().isHorizontal()
                ? WeirdEquipmentBlocks.WALL_ROPE.get().defaultBlockState().setValue(WallRopeBlock.FACING, side.getOpposite())
                : WeirdEquipmentBlocks.ROPE.get().defaultBlockState();
        if (!rope.canSurvive(level, pos)) {
            dropCoil(level);
            return;
        }

        level.setBlockAndUpdate(pos, rope);
        ropesLeft--;
        ropePos = pos;
        isPlacing = true;

        // Freeze the projectile in place while the remaining ropes unroll in tick().
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (this.level() instanceof ServerLevel level) {
            dropCoil(level);
        }
    }

    @Override
    public void tick() {
        if (isPlacing && this.level() instanceof ServerLevel level) {
            if (ticksUntilNextRope > 0) {
                ticksUntilNextRope--;
            } else {
                ticksUntilNextRope = TICKS_PER_ROPE - 1;
                placeNextRope(level);
            }
        }
        super.tick();
    }

    private void placeNextRope(ServerLevel level) {
        BlockPos below = ropePos.below();
        if (ropesLeft > 0 && level.getBlockState(below).isAir()) {
            ropePos = below;
            level.setBlockAndUpdate(ropePos, WeirdEquipmentBlocks.ROPE.get().defaultBlockState());
            ropesLeft--;
            return;
        }
        // Out of room or out of rope: drop whatever is left at the end of the rope.
        if (ropesLeft > 0) {
            Vec3 dropPos = Vec3.atBottomCenterOf(ropePos).add(0, 0.5, 0);
            level.addFreshEntity(new ItemEntity(level, dropPos.x, dropPos.y, dropPos.z,
                    new ItemStack(WeirdEquipmentItems.ROPE.get(), ropesLeft)));
        }
        this.discard();
    }

    private void dropCoil(ServerLevel level) {
        this.spawnAtLocation(level, this.getItem().copy());
        this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return WeirdEquipmentItems.SMALL_ROPE_COIL.get();
    }
}
