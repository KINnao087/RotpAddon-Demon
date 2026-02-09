package com.inza.demonaddon.power;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FearPowerProvider implements INBTSerializable<INBT>, ICapabilityProvider {
    private FearPower fearPower = new FearPower();

    @CapabilityInject(FearPower.class)
    public static final Capability<FearPower> FEAR_POWER_CAPABILITY = null;

    private final LazyOptional<FearPower> opt = LazyOptional.of(() -> this.fearPower);



    public FearPowerProvider() {}

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        fearPower.loadNBTData(nbt); // write fields into nbt
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        fearPower.saveNBTData((CompoundNBT) nbt); // read fields from nbt
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == FEAR_POWER_CAPABILITY) {
            return opt.cast();
        } return LazyOptional.empty();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == FEAR_POWER_CAPABILITY) {
            return opt.cast();
        } return LazyOptional.empty();
    }
}
