package systems.alkaline.weirdequipment.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.material.Fluids;
import systems.alkaline.weirdequipment.WeirdEquipment;
import systems.alkaline.weirdequipment.item.*;
import systems.alkaline.weirdequipment.item.material.WeirdEquipmentMaterials;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class WeirdEquipmentItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WeirdEquipment.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> JACK_O_HELMET = register("jack_o_helmet", Item::new, p -> p
            .humanoidArmor(WeirdEquipmentMaterials.PUMPKIN, ArmorType.HELMET)
            .arch$tab(CreativeModeTabs.COMBAT));

    public static final RegistrySupplier<Item> CACTUS_SWORD = register("cactus_sword", CactusSwordItem::new, p -> p
            .sword(WeirdEquipmentMaterials.CACTUS, 1.0f, -2.0f)
            .arch$tab(CreativeModeTabs.COMBAT));
    public static final RegistrySupplier<Item> DIRT_SWORD = register("dirt_sword", Item::new, p -> p
            .sword(ToolMaterial.WOOD, -3.0f, -2.6f)
            .arch$tab(CreativeModeTabs.COMBAT));

    public static final RegistrySupplier<Item> FLINT_AND_SHEARS = register("flint_and_shears", FlintAndShearsItem::new, p -> p
            .durability(300)
            .component(DataComponents.TOOL, ShearsItem.createToolProperties())
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    public static final RegistrySupplier<Item> SELF_SLINGSHOT = register("self_slingshot", SelfSlingshotItem::new, p -> p
            .durability(350)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    public static final RegistrySupplier<Item> BLOCK_CANNON = register("block_cannon", BlockCannonItem::new, p -> p
            .durability(640)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    public static final RegistrySupplier<Item> DIAMOND_SHEARS = register("diamond_shears", ShearsItem::new, p -> p
            .durability(1000)
            .component(DataComponents.TOOL, ShearsItem.createToolProperties())
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    public static final RegistrySupplier<Item> NETHERITE_TORCH_PICKAXE = register("netherite_torch_pickaxe", NetheriteTorchPickaxeItem::new, p -> p
            .pickaxe(ToolMaterial.NETHERITE, 1.0f, -2.8f)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    public static final RegistrySupplier<Item> BOTTOMLESS_WATER_BUCKET = register("bottomless_water_bucket",
            p -> new BottomlessBucketItem(Fluids.WATER, p), p -> p
                    .stacksTo(1)
                    .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    public static final RegistrySupplier<Item> LARGE_LAVA_BUCKET = register("large_lava_bucket",
            p -> new LargeBucketItem(Fluids.LAVA, p), p -> p
                    .durability(9)
                    .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    public static final RegistrySupplier<Item> TORCH_BOW = register("torch_bow", TorchBowItem::new, p -> p
            .durability(384)
            .enchantable(1)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    public static final RegistrySupplier<Item> TORCH_CORE = register("torch_core", Item::new, p -> p
            .arch$tab(CreativeModeTabs.INGREDIENTS));

    public static final RegistrySupplier<Item> ROPE = register("rope",
            p -> new StandingAndWallBlockItem(WeirdEquipmentBlocks.ROPE.get(), WeirdEquipmentBlocks.WALL_ROPE.get(), Direction.UP, p), p -> p
                    .useBlockDescriptionPrefix()
                    .arch$tab(CreativeModeTabs.BUILDING_BLOCKS));

    public static final RegistrySupplier<Item> SMALL_ROPE_COIL = register("small_rope_coil", p -> new RopeCoilItem(p, 9), p -> p
            .stacksTo(32)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    public static final RegistrySupplier<Item> LARGE_ROPE_COIL = register("large_rope_coil", p -> new RopeCoilItem(p, 18), p -> p
            .stacksTo(32)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    public static final RegistrySupplier<Item> XL_ROPE_COIL = register("xl_rope_coil", p -> new RopeCoilItem(p, 36), p -> p
            .stacksTo(16)
            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));

    /** Items must know their registry id before they're constructed (since 1.21.2). */
    private static RegistrySupplier<Item> register(String name, Function<Item.Properties, ? extends Item> factory, UnaryOperator<Item.Properties> properties) {
        return ITEMS.register(name, () -> factory.apply(properties.apply(
                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, WeirdEquipment.id(name))))));
    }

    public static void register() {
        ITEMS.register();
    }
}
