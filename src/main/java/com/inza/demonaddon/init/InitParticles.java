package com.inza.demonaddon.init;

import com.inza.demonaddon.AddonMain;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AddonMain.MOD_ID);

    public static final RegistryObject<BasicParticleType> DOMAIN_BURST_RED =
            PARTICLES.register("domain_burst_red", () -> new BasicParticleType(true));
}
