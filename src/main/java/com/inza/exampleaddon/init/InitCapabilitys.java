package com.inza.exampleaddon.init;

import com.inza.exampleaddon.AddonMain;
import com.inza.exampleaddon.power.FearPower;
import com.inza.exampleaddon.power.FearPowerProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class InitCapabilitys {
    public static void register(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(
                FearPower.class,
                new Capability.IStorage<FearPower>() {
                    @Override
                    public INBT writeNBT(Capability<FearPower> capability, FearPower instance, Direction side) {
                        CompoundNBT tag = new CompoundNBT();
                        instance.loadNBTData(tag);
                        return tag;
                    }

                    @Override
                    public void readNBT(Capability<FearPower> capability, FearPower instance, Direction side, INBT nbt) {
                        if (nbt instanceof CompoundNBT) {
                            instance.saveNBTData((CompoundNBT) nbt);
                        }
                    }
                },
                FearPower::new
        );
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(InitCapabilitys::register);
    }
}
