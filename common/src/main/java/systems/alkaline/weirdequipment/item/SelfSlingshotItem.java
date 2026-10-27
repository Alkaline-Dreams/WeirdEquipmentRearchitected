package systems.alkaline.weirdequipment.item;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import systems.alkaline.weirdequipment.config.WeirdEquipmentConfig;

/** A slingshot that launches the player away from the block they're looking at. */
public class SelfSlingshotItem extends BowItem {
    public SelfSlingshotItem(Properties properties) {
        super(properties);
    }

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
        if (player.pick(player.blockInteractionRange(), 0, false).getType() == HitResult.Type.BLOCK) {
            // The server launches the player and sends them the new motion, so the server's config decides the strength.
            if (player instanceof ServerPlayer serverPlayer) {
                double strength = power * WeirdEquipmentConfig.SELF_SLINGSHOT_VELOCITY;
                player.setDeltaMovement(player.getLookAngle().multiply(-2.25 * strength, -1.25 * strength, -2.25 * strength));
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
                itemStack.hurtAndBreak(1, player, player.getUsedItemHand());
            }
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
