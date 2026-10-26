package xyz.venividivivi.weirdequipment.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import xyz.venividivivi.weirdequipment.config.WeirdEquipmentConfig;
import xyz.venividivivi.weirdequipment.entity.TorchArrowEntity;
import xyz.venividivivi.weirdequipment.registry.WeirdEquipmentItems;

import java.util.List;
import java.util.function.Predicate;

/**
 * A bow that shoots torches. Ammo comes from torches in the inventory, or from a
 * Netherite Torch Pickaxe held in the main hand (which costs pickaxe durability).
 */
public class TorchBowItem extends BowItem {
    private static final Predicate<ItemStack> TORCH_ONLY = stack -> stack.is(Items.TORCH);

    public TorchBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return TORCH_ONLY;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack bow = player.getItemInHand(hand);
        boolean hasAmmo = !player.getProjectile(bow).isEmpty() || hasInfinity(level, bow) || holdsTorchPickaxe(player);
        if (!hasAmmo) {
            return InteractionResult.FAIL;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack bow, Level level, LivingEntity entity, int remainingTime) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        float power = getPowerForTime(this.getUseDuration(bow, entity) - remainingTime);
        if (power < 0.1f) {
            return false;
        }

        ItemStack ammo = player.getProjectile(bow);
        boolean infinite = hasInfinity(level, bow);
        boolean fromPickaxe = false;
        List<ItemStack> projectiles;
        if (!ammo.isEmpty() && !infinite) {
            // Uses up a torch (except in creative).
            projectiles = draw(bow, ammo, player);
        } else if (infinite) {
            projectiles = List.of(freeTorch());
        } else if (holdsTorchPickaxe(player)) {
            projectiles = List.of(freeTorch());
            fromPickaxe = true;
        } else {
            return false;
        }

        if (level instanceof ServerLevel serverLevel && !projectiles.isEmpty()) {
            this.shoot(serverLevel, player, player.getUsedItemHand(), bow, projectiles, power * 3.0f, 1.0f, power == 1.0f, null);
            if (fromPickaxe) {
                player.getMainHandItem().hurtAndBreak(WeirdEquipmentConfig.NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS, player, EquipmentSlot.MAINHAND);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);
        player.awardStat(Stats.ITEM_USED.get(this));
        return true;
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
        // In creative with no torches the "ammo" is a vanilla arrow; always carry a torch instead.
        ItemStack pickup = new ItemStack(Items.TORCH);
        if (projectile.has(DataComponents.INTANGIBLE_PROJECTILE)) {
            pickup.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
        }
        TorchArrowEntity torch = new TorchArrowEntity(level, shooter, pickup, weapon);
        torch.setBaseDamage(1.0);
        if (isCrit) {
            torch.setCritArrow(true);
        }
        return torch;
    }

    /** A torch that can't be picked back up, so free shots don't duplicate torches. */
    private static ItemStack freeTorch() {
        ItemStack torch = new ItemStack(Items.TORCH);
        torch.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
        return torch;
    }

    private static boolean holdsTorchPickaxe(Player player) {
        return player.getMainHandItem().is(WeirdEquipmentItems.NETHERITE_TORCH_PICKAXE.get());
    }

    // Vanilla Infinity only works on arrows, so check for it ourselves.
    private static boolean hasInfinity(Level level, ItemStack bow) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.INFINITY)
                .map(infinity -> EnchantmentHelper.getItemEnchantmentLevel(infinity, bow) > 0)
                .orElse(false);
    }
}
