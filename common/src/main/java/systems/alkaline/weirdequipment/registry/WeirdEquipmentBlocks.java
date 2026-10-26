package systems.alkaline.weirdequipment.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import systems.alkaline.weirdequipment.WeirdEquipment;
import systems.alkaline.weirdequipment.block.RopeBlock;
import systems.alkaline.weirdequipment.block.WallRopeBlock;

import java.util.function.Function;

public class WeirdEquipmentBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(WeirdEquipment.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> ROPE = register("rope", RopeBlock::new);
    public static final RegistrySupplier<Block> WALL_ROPE = register("wall_rope", WallRopeBlock::new);

    private static RegistrySupplier<Block> register(String name, Function<BlockBehaviour.Properties, Block> factory) {
        return BLOCKS.register(name, () -> factory.apply(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, WeirdEquipment.id(name)))
                .mapColor(MapColor.COLOR_BROWN)
                .dynamicShape()
                .sound(SoundType.WOOL)));
    }

    public static void register() {
        BLOCKS.register();
    }
}
