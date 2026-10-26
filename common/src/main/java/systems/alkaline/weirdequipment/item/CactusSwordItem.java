package xyz.venividivivi.weirdequipment.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** A strong early-game sword that pricks its wielder on every hit. */
public class CactusSwordItem extends Item {
    public CactusSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        if (attacker.level() instanceof ServerLevel level) {
            attacker.hurtServer(level, attacker.damageSources().cactus(), 1);
        }
        super.hurtEnemy(itemStack, mob, attacker);
    }
}
