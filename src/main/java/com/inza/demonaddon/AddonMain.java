package com.inza.demonaddon;

import com.inza.demonaddon.action.freeze.network.FreezeNetwork;
import com.inza.demonaddon.init.*;

import com.inza.demonaddon.power.FearPowerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

// Your addon's main file

@Mod(AddonMain.MOD_ID)
public class AddonMain {
//    public static Logger LOGGER = LogManager.getLogger();

    // The mod's id. Used quite often, mostly when creating ResourceLocation (objects).
    // Its value should match the "modid" entry in the META-INF/mods.toml file
    public static final String MOD_ID = "demonaddon";

    public AddonMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // All DeferredRegister objects are registered here.
        // A DeferredRegister needs to be created for each type of objects that need to be registered in the game 
        // (see ForgeRegistries or JojoCustomRegistries)
        InitEntities.ENTITIES.register(modEventBus);
        InitParticles.PARTICLES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);

        InitCapabilities.init(modEventBus);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::attachCapability);

        AddonNetwork.init();
        FreezeNetwork.init();
    }

    public void attachCapability(AttachCapabilitiesEvent<LivingEntity> event) {
        if (event.getObject() != null) {
            LivingEntity entity = (LivingEntity) event.getObject();
            if (!entity.getCapability(FearPowerProvider.FEAR_POWER_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(AddonMain.MOD_ID, "fear_power"), new FearPowerProvider());
            }
        }
    }
}
