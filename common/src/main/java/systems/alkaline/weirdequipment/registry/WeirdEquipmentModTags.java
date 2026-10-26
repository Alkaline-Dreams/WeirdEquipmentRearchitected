package xyz.venividivivi.weirdequipment.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import xyz.venividivivi.weirdequipment.WeirdEquipment;

public class WeirdEquipmentModTags {
    /** Convention tag shared by Fabric and NeoForge mods. */
    public static final TagKey<Item> SHEARS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/shear"));
    public static final TagKey<Item> REPAIRS_PUMPKIN_ARMOR = TagKey.create(Registries.ITEM, WeirdEquipment.id("repairs_pumpkin_armor"));
    public static final TagKey<Item> CACTUS_TOOL_MATERIALS = TagKey.create(Registries.ITEM, WeirdEquipment.id("cactus_tool_materials"));
}
