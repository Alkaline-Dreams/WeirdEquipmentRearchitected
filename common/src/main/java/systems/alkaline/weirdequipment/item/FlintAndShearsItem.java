package xyz.venividivivi.weirdequipment.item;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

/** Shears that also work as flint and steel. */
public class FlintAndShearsItem extends ShearsItem {
    public FlintAndShearsItem(Properties properties) {
        super(properties);
    }

    // Mirrors FlintAndSteelItem#useOn.
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        // Leave pumpkins to the shears behavior instead of setting them on fire.
        if (state.is(Blocks.PUMPKIN)) {
            return InteractionResult.SUCCESS;
        }

        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }

        BlockPos firePos = pos.relative(context.getClickedFace());
        if (!BaseFireBlock.canBePlacedAt(level, firePos, context.getHorizontalDirection())) {
            return InteractionResult.FAIL;
        }

        level.playSound(player, firePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
        level.setBlock(firePos, BaseFireBlock.getState(level, firePos), 11);
        level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        ItemStack itemStack = context.getItemInHand();
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, firePos, itemStack);
            itemStack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
        }
        return InteractionResult.SUCCESS;
    }
}
