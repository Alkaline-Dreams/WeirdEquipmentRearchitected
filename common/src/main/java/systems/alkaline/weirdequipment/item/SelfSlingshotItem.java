package xyz.venividivivi.weirdequipment.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import xyz.venividivivi.weirdequipment.config.WeirdEquipmentConfig;

/** A slingshot that launches the player away from the block they're looking at. */
public class SelfSlingshotItem extends BowItem {
    private static final double MAX_REACH = 5.0;

    public SelfSlingshotItem(Properties properties) {
        super(properties);
    }

    // Runs on both sides: the client moves the player, the server handles durability.
    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        float power = getPowerForTime(this.getUseDuration(itemStack, entity) - remainingTime);
        if (power < 0.1f) {
            return false;
        }

        float soundPitch = 0.05f;
        if (player.pick(MAX_REACH, 0, false).getType() == HitResult.Type.BLOCK) {
            power *= WeirdEquipmentConfig.SELF_SLINGSHOT_VELOCITY;
            float yaw = player.getYRot() * Mth.DEG_TO_RAD;
            float pitch = player.getXRot() * Mth.DEG_TO_RAD;
            Vec3 look = new Vec3(-Mth.sin(yaw) * Mth.cos(pitch), -Mth.sin(pitch), Mth.cos(yaw) * Mth.cos(pitch)).normalize();
            player.setDeltaMovement(look.multiply(-2.25 * power, -1.25 * power, -2.25 * power));

            itemStack.hurtAndBreak(1, player, player.getUsedItemHand());
            soundPitch = 1.0f;
        }
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0f, soundPitch / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);
        return true;
    }

    // Unlike a bow, no ammo is needed.
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }
}
