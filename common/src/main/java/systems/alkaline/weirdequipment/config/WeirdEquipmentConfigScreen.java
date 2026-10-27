package systems.alkaline.weirdequipment.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static systems.alkaline.weirdequipment.config.WeirdEquipmentConfig.*;

/** Config screen built with Cloth Config. Only call this when Cloth Config is installed. */
public class WeirdEquipmentConfigScreen {
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.weird_equipment.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.getOrCreateCategory(Component.translatable("config.weird_equipment.category.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(Component.translatable("config.weird_equipment.entry.torch_bow_can_damage_mobs"), TORCH_BOW_CAN_DAMAGE_MOBS)
                        .setDefaultValue(DEFAULT_TORCH_BOW_CAN_DAMAGE_MOBS)
                        .setSaveConsumer(value -> TORCH_BOW_CAN_DAMAGE_MOBS = value)
                        .build())
                .addEntry(entryBuilder
                        .startIntSlider(Component.translatable("config.weird_equipment.entry.netherite_torch_pickaxe_durability_loss"), NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS,
                                MIN_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS, MAX_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS)
                        .setDefaultValue(DEFAULT_NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS)
                        .setSaveConsumer(value -> NETHERITE_TORCH_PICKAXE_DURABILITY_LOSS = value)
                        .build())
                .addEntry(entryBuilder
                        .startFloatField(Component.translatable("config.weird_equipment.entry.self_slingshot_velocity"), SELF_SLINGSHOT_VELOCITY)
                        .setDefaultValue(DEFAULT_SELF_SLINGSHOT_VELOCITY)
                        .setMin(MIN_SELF_SLINGSHOT_VELOCITY)
                        .setMax(MAX_SELF_SLINGSHOT_VELOCITY)
                        .setSaveConsumer(value -> SELF_SLINGSHOT_VELOCITY = value)
                        .build());
        builder.setSavingRunnable(WeirdEquipmentConfig::save);
        return builder.build();
    }
}
