package com.inza.demonaddon.init;

import com.inza.demonaddon.power.FearPower;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class InitCapabilities {
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
        modBus.addListener(InitCapabilities::register);
    }
}
