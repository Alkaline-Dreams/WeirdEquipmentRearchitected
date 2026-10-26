package systems.alkaline.weirdequipment.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Config screen built with Cloth Config. Only call this when Cloth Config is installed. */
public class WeirdEquipmentConfigScreen {
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.weird_equipment.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.getOrCreateCategory(Component.translatable("config.weird_equipment.category.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(Component.translatable("config.weird_equipment.entry.torch_bow_can_damage_mobs"), WeirdEquipmentConfig.TORCH_BOW_CAN_DAMAGE_MOBS)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> WeirdEquipmentConfig.TORCH_BOW_CAN_DAMAGE_MOBS = value)
                        .build())
                .addEntry(entryBuilder
                        .startIntSlider(Component.translatable("config.weird_equipment.entry.netherite_torch_pickaxe_durability_loss"), WeirdEquipmentConfig.NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS, 0, 10)
                        .setDefaultValue(5)
                        .setSaveConsumer(value -> WeirdEquipmentConfig.NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = value)
                        .build())
                .addEntry(entryBuilder
                        .startFloatField(Component.translatable("config.weird_equipment.entry.self_slingshot_velocity"), WeirdEquipmentConfig.SELF_SLINGSHOT_VELOCITY)
                        .setDefaultValue(1)
                        .setSaveConsumer(value -> WeirdEquipmentConfig.SELF_SLINGSHOT_VELOCITY = value)
                        .build());
        builder.setSavingRunnable(WeirdEquipmentConfig::save);
        return builder.build();
    }
}
