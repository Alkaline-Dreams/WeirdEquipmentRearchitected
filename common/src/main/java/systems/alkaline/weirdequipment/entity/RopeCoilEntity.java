package systems.alkaline.weirdequipment.entity;

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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import systems.alkaline.weirdequipment.block.WallRopeBlock;
import systems.alkaline.weirdequipment.item.RopeCoilItem;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentBlocks;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentEntityTypes;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentItems;

/**
 * A thrown rope coil. When it hits a block it freezes in place and unrolls its ropes
 * downward one at a time.
 */
public class RopeCoilEntity extends ThrowableItemProjectile {
    private static final int TICKS_PER_ROPE = 3;

    /** The lowest rope placed so far, or null while the coil is still flying. */
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

    private boolean isPlacing() {
        return ropePos != null;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        // Once frozen in place, ignore further hits.
        if (isPlacing() || !(this.level() instanceof ServerLevel level)) return;

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
        if (isPlacing() && this.level() instanceof ServerLevel level) {
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
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("ropes_left", ropesLeft);
        output.putInt("ticks_until_next_rope", ticksUntilNextRope);
        output.storeNullable("rope_pos", BlockPos.CODEC, ropePos);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        ropesLeft = input.getIntOr("ropes_left", 0);
        ticksUntilNextRope = input.getIntOr("ticks_until_next_rope", 0);
        ropePos = input.read("rope_pos", BlockPos.CODEC).orElse(null);
    }

    @Override
    protected Item getDefaultItem() {
        return WeirdEquipmentItems.SMALL_ROPE_COIL.get();
    }
}
