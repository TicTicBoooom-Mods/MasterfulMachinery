package com.ticticboooom.mods.mm.setup;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.client.container.BlueprintContainer;
import com.ticticboooom.mods.mm.client.container.ControllerContainer;
import com.ticticboooom.mods.mm.client.container.PortContainer;
import com.ticticboooom.mods.mm.client.container.ProjectorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Ref.MOD_ID);

    public static final RegistryObject<ContainerType<ControllerContainer>> CONTROLLER = CONTAINERS.register("controller", () -> IForgeContainerType.create(ControllerContainer::new));
    public static final RegistryObject<ContainerType<PortContainer>> PORT = CONTAINERS.register("port", () -> IForgeContainerType.create(PortContainer::new));
    public static final RegistryObject<ContainerType<BlueprintContainer>> BLUEPRINT = CONTAINERS.register("blueprint", () -> IForgeContainerType.create(BlueprintContainer::new));
    public static final RegistryObject<ContainerType<ProjectorContainer>> PROJECTOR = CONTAINERS.register("projector", () -> IForgeContainerType.create(ProjectorContainer::new));
}
