package com.ticticboooom.mods.mm.setup;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.process.ModifierAction;
import com.ticticboooom.mods.mm.process.PreProcessorType;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import com.ticticboooom.mods.mm.process.ingredients.ItemsIngredientType;
import com.ticticboooom.mods.mm.process.modifieractions.NothingModifierAction;
import com.ticticboooom.mods.mm.process.modifieractions.SpeedModifierAction;
import com.ticticboooom.mods.mm.process.outputs.ItemsOutputType;
import com.ticticboooom.mods.mm.process.preprocessors.ModifiablePreProcessorType;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.keys.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MMRegistries {

    public static IForgeRegistry<StructureKeyType> STRUCTURE_KEY_TYPES;
    public static IForgeRegistry<ProcessIngredientType> PROCESS_INGREDIENT_TYPES;
    public static IForgeRegistry<ProcessOutputType> PROCESS_OUTPUT_TYPES;
    public static IForgeRegistry<PreProcessorType> PREPROCESSOR_TYPES;
    public static IForgeRegistry<ModifierAction> MODIFIER_ACTIONS;

    @SubscribeEvent
    public static void on(RegistryEvent.NewRegistry event) {
        STRUCTURE_KEY_TYPES = new RegistryBuilder<StructureKeyType>().setName(Ref.Reg.STRUCTURE_KEY_TYPE).setType(StructureKeyType.class).create();
        PROCESS_INGREDIENT_TYPES = new RegistryBuilder<ProcessIngredientType>().setName(Ref.Reg.PROCESS_INGREDIENT_TYPE).setType(ProcessIngredientType.class).create();
        PROCESS_OUTPUT_TYPES = new RegistryBuilder<ProcessOutputType>().setName(Ref.Reg.PROCESS_OUTPUT_TYPE).setType(ProcessOutputType.class).create();
        PREPROCESSOR_TYPES = new RegistryBuilder<PreProcessorType>().setName(Ref.Reg.PREPROCESSOR_TYPE).setType(PreProcessorType.class).create();
        MODIFIER_ACTIONS = new RegistryBuilder<ModifierAction>().setName(Ref.Reg.MODIFIER_ACTIONS).setType(ModifierAction.class).create();
    }

    @SubscribeEvent
    public static void registerSKT(RegistryEvent.Register<StructureKeyType> event) {
        event.getRegistry().registerAll(
                new BlockStructureKeyType().setRegistryName(Ref.Reg.SKT.BLOCK),
                new PortStructureKeyType().setRegistryName(Ref.Reg.SKT.PORT),
                new PortTierStructureKeyType().setRegistryName(Ref.Reg.SKT.PORT_TIER),
                new PortGroupStructureKeyType().setRegistryName(Ref.Reg.SKT.PORT_GROUP),
                new ModifiableStructureKeyType().setRegistryName(Ref.Reg.SKT.MODIFIABLE)
        );
    }

    @SubscribeEvent
    public static void registerPIT(RegistryEvent.Register<ProcessIngredientType> event) {
        event.getRegistry().registerAll(
                new ItemsIngredientType().setRegistryName(Ref.Reg.PIT.ITEMS)
        );
    }

    @SubscribeEvent
    public static void registerPOT(RegistryEvent.Register<ProcessOutputType> event) {
        event.getRegistry().registerAll(
                new ItemsOutputType().setRegistryName(Ref.Reg.POT.ITEMS)
        );
    }

    @SubscribeEvent
    public static void registerPT(RegistryEvent.Register<PreProcessorType> event) {
        event.getRegistry().registerAll(
                new ModifiablePreProcessorType().setRegistryName(Ref.Reg.PT.MODIFIABLE)
        );
    }

    @SubscribeEvent
    public static void registerMA(RegistryEvent.Register<ModifierAction> event) {
        event.getRegistry().registerAll(
                new NothingModifierAction().setRegistryName(Ref.Reg.MA.NOTHING),
                new SpeedModifierAction().setRegistryName(Ref.Reg.MA.SPEED)
        );
    }
}
