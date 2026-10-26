package xyz.venividivivi.weirdequipment.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import xyz.venividivivi.weirdequipment.config.WeirdEquipmentConfig;

/** A netherite pickaxe that places torches on right-click, at the cost of durability. */
public class NetheriteTorchPickaxeItem extends Item {
    public NetheriteTorchPickaxeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Direction side = context.getClickedFace();
        BlockPos pos = context.getClickedPos().relative(side);
        BlockState torch = side.getAxis().isHorizontal()
                ? Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, side)
                : Blocks.TORCH.defaultBlockState();

        if (!level.getBlockState(pos).isAir() || !torch.canSurvive(level, pos)) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        level.playSound(player, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0f, 0.9f);
        level.setBlockAndUpdate(pos, torch);
        level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        if (player != null) {
            context.getItemInHand().hurtAndBreak(WeirdEquipmentConfig.NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS, player, context.getHand());
        }
        return InteractionResult.SUCCESS;
    }
}
