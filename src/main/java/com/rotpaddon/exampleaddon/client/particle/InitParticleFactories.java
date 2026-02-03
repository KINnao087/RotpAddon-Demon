package com.rotpaddon.exampleaddon.client.particle;

import com.rotpaddon.exampleaddon.AddonMain;
import com.rotpaddon.exampleaddon.init.InitParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitParticleFactories {
    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(InitParticles.DOMAIN_BURST_RED.get(), DomainBurstRedParticle.Factory::new);
    }
}
