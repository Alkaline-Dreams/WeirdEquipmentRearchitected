package systems.alkaline.weirdequipment.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentEntityTypes;
import systems.alkaline.weirdequipment.util.AimedBlockPlaceContext;

/**
 * A block fired from the Block Cannon. Places itself where it lands, facing the way the shooter was
 * looking when they fired. Drops as an item if it can't be placed or hits an entity.
 */
public class BlockCannonShotEntity extends ThrowableItemProjectile {
    /** The shooter's aim at fire time. Server-side only, since that's where placement happens. */
    private AimedBlockPlaceContext.Aim aim = null;

    public BlockCannonShotEntity(EntityType<? extends BlockCannonShotEntity> type, Level level) {
        super(type, level);
    }

    public BlockCannonShotEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(WeirdEquipmentEntityTypes.BLOCK_CANNON_SHOT.get(), owner, level, stack);
        this.aim = AimedBlockPlaceContext.Aim.capture(owner);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (!(this.level() instanceof ServerLevel level)) return;

        ItemStack stack = this.getItem().copy();
        if (aim != null && stack.getItem() instanceof BlockItem blockItem) {
            Player player = this.getOwner() instanceof Player p ? p : null;
            if (blockItem.place(new AimedBlockPlaceContext(level, player, stack, hitResult, aim)).consumesAction()) {
                this.discard();
                return;
            }
        }
        dropShot(level);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (this.level() instanceof ServerLevel level) {
            dropShot(level);
        }
    }

    private void dropShot(ServerLevel level) {
        this.spawnAtLocation(level, this.getItem().copy());
        this.discard();
    }

    /** A third of a normal thrown item's gravity (0.03), for a flatter arc. */
    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.STONE;
    }
}
