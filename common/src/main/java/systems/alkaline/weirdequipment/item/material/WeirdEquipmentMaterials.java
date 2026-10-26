package systems.alkaline.weirdequipment.item.material;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import systems.alkaline.weirdequipment.WeirdEquipment;
import systems.alkaline.weirdequipment.registry.WeirdEquipmentModTags;

import java.util.Map;

public final class WeirdEquipmentMaterials {
    public static final ToolMaterial CACTUS = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL, 150, 2.0f, 4.5f, 8, WeirdEquipmentModTags.CACTUS_TOOL_MATERIALS);

    public static final ResourceKey<EquipmentAsset> PUMPKIN_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, WeirdEquipment.id("pumpkin"));

    public static final ArmorMaterial PUMPKIN = new ArmorMaterial(
            14, Map.of(ArmorType.HELMET, 1), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0f, 0.0f,
            WeirdEquipmentModTags.REPAIRS_PUMPKIN_ARMOR, PUMPKIN_ASSET);

    private WeirdEquipmentMaterials() {
    }
}
