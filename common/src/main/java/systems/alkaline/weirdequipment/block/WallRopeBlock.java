package systems.alkaline.weirdequipment.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/** A rope tied to the side of a block. FACING points toward the supporting block. */
public class WallRopeBlock extends HorizontalDirectionalBlock {
    private static final VoxelShape HANGING_PART = Block.box(6.8, 0, 6.8, 9.2, 8, 9.2);
    private static final VoxelShape NORTH = Shapes.or(HANGING_PART, Block.box(6.8, 6.8, 0, 9.2, 9.2, 9.2));
    private static final VoxelShape EAST = Shapes.or(HANGING_PART, Block.box(6.8, 6.8, 6.8, 16, 9.2, 9.2));
    private static final VoxelShape SOUTH = Shapes.or(HANGING_PART, Block.box(6.8, 6.8, 6.8, 9.2, 9.2, 16));
    private static final VoxelShape WEST = Shapes.or(HANGING_PART, Block.box(0, 6.8, 6.8, 9.2, 9.2, 9.2));

    public WallRopeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction side = context.getClickedFace();
        if (side.getAxis().isHorizontal()) {
            BlockState state = this.defaultBlockState().setValue(FACING, side.getOpposite());
            if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing);
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing.getOpposite());
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        RopeBlock.breakNextTickIfUnsupported(state, level, pos);
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        RopeBlock.breakIfUnsupported(state, level, pos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
