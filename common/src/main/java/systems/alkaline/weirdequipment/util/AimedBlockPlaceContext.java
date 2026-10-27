package systems.alkaline.weirdequipment.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Arrays;

/**
 * A block placement context captured from a player and stored for future use,
 * instead of being read from the live player. Used for block cannon's placement.
 */
public class AimedBlockPlaceContext extends BlockPlaceContext {
    private final Aim aim;

    public AimedBlockPlaceContext(Level level, Player player, ItemStack stack, BlockHitResult hitResult, Aim aim) {
        super(level, player, InteractionHand.OFF_HAND, stack, hitResult);
        this.aim = aim;
    }

    @Override
    public Direction getHorizontalDirection() {
        return aim.horizontal();
    }

    @Override
    public Direction getNearestLookingVerticalDirection() {
        return aim.vertical();
    }

    @Override
    public Direction getNearestLookingDirection() {
        return aim.nearest()[0];
    }

    @Override
    public float getRotation() {
        return aim.yRot();
    }

    @Override
    public boolean isSecondaryUseActive() {
        return aim.secondaryUse();
    }

    /** Like vanilla: when placing against a face, the direction into that face is tried first. */
    @Override
    public Direction[] getNearestLookingDirections() {
        Direction[] directions = aim.nearest();
        if (this.replaceClicked) return directions;

        Direction intoFace = this.getClickedFace().getOpposite();
        Direction[] reordered = new Direction[directions.length];
        reordered[0] = intoFace;
        int i = 1;
        for (Direction direction : directions) {
            if (direction != intoFace) reordered[i++] = direction;
        }
        return reordered;
    }

    /** The answers vanilla gives for an entity's look at one moment. */
    public record Aim(Direction[] nearest, Direction horizontal, Direction vertical, float yRot, boolean secondaryUse) {
        public static final Codec<Aim> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Direction.CODEC.listOf(6, 6).xmap(list -> list.toArray(Direction[]::new), Arrays::asList)
                        .fieldOf("nearest").forGetter(Aim::nearest),
                Direction.CODEC.fieldOf("horizontal").forGetter(Aim::horizontal),
                Direction.CODEC.fieldOf("vertical").forGetter(Aim::vertical),
                Codec.FLOAT.fieldOf("y_rot").forGetter(Aim::yRot),
                Codec.BOOL.fieldOf("secondary_use").forGetter(Aim::secondaryUse)
        ).apply(instance, Aim::new));

        public static Aim capture(LivingEntity entity) {
            return new Aim(
                    Direction.orderedByNearest(entity),
                    entity.getDirection(),
                    Direction.getFacingAxis(entity, Direction.Axis.Y),
                    entity.getYRot(),
                    entity instanceof Player player && player.isSecondaryUseActive());
        }

        @Override
        public Direction[] nearest() {
            return nearest.clone();
        }
    }
}
