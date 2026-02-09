package com.inza.exampleaddon.init;

import com.inza.exampleaddon.AddonMain;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITIES, AddonMain.MOD_ID);
    
    
    
//    public static final RegistryObject<EntityType<ExamplePickaxeEntity>> EXAMPLE_PICKAXE = ENTITIES.register("example_pickaxe",
//            () -> EntityType.Builder.<ExamplePickaxeEntity>of(ExamplePickaxeEntity::new, EntityClassification.MISC)
//            .sized(1.0F, 1.0F)
//            .build(AddonMain.MOD_ID + ":example_pickaxe"));
};
