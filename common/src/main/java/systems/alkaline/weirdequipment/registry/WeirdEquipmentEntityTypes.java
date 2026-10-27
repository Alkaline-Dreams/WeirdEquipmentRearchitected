package systems.alkaline.weirdequipment.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import systems.alkaline.weirdequipment.WeirdEquipment;
import systems.alkaline.weirdequipment.entity.BlockCannonShotEntity;
import systems.alkaline.weirdequipment.entity.RopeCoilEntity;
import systems.alkaline.weirdequipment.entity.TorchArrowEntity;

public class WeirdEquipmentEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(WeirdEquipment.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<TorchArrowEntity>> TORCH_ARROW = ENTITY_TYPES.register("torch_arrow", () ->
            EntityType.Builder.<TorchArrowEntity>of(TorchArrowEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f)
                    .fireImmune()
                    .noSummon()
                    .noLootTable()
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(key("torch_arrow")));

    public static final RegistrySupplier<EntityType<RopeCoilEntity>> ROPE_COIL = ENTITY_TYPES.register("rope_coil", () ->
            EntityType.Builder.<RopeCoilEntity>of(RopeCoilEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f)
                    .noSummon()
                    .noLootTable()
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(key("rope_coil")));

    public static final RegistrySupplier<EntityType<BlockCannonShotEntity>> BLOCK_CANNON_SHOT = ENTITY_TYPES.register("block_cannon_shot", () ->
            EntityType.Builder.<BlockCannonShotEntity>of(BlockCannonShotEntity::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f)
                    .noSummon()
                    .noLootTable()
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(key("block_cannon_shot")));

    private static ResourceKey<EntityType<?>> key(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, WeirdEquipment.id(name));
    }

    public static void register() {
        ENTITY_TYPES.register();
    }
}
