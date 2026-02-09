package com.inza.exampleaddon.power;

import net.minecraft.nbt.CompoundNBT;

public class FearPower {
    private float fear;
    private float maxFear;
    private float fearCostPerTick; // decay/consumption per tick (20 ticks = 1s)

    public FearPower() {
        fear = 0;
        maxFear = 100f;
        fearCostPerTick = 0f; // default 1 per second
    }

    public float getFear() { return fear; }
    public void setFear(float fear) { this.fear = fear; }
    public float getMaxFear() { return maxFear; }
    public void setMaxFear(float maxFear) { this.maxFear = maxFear; }

    // Decay / per-tick consumption
    public float getFearCostPerTick() { return fearCostPerTick; }
    public void setFearCostPerTick(float fearCostPerTick) { this.fearCostPerTick = Math.max(0, fearCostPerTick); }
    /** Adjust per-tick cost; clamps to >=0. */
    public void addFearCostPerTick(float delta) { this.fearCostPerTick = Math.max(0, this.fearCostPerTick + delta); }
    /** Reduce per-tick cost; clamps to >=0. */
    public void subFearCostPerTick(float delta) { this.fearCostPerTick = Math.max(0, this.fearCostPerTick - Math.max(0, delta)); }

    public void addFear(float val) {
        this.fear += val;
        this.fear = Math.min(this.fear, maxFear);
        this.fear = Math.max(this.fear, 0);
    }
    public boolean subFear(float val) {
        boolean retVal = true;

        this.fear -= val;
        this.fear = Math.min(this.fear, maxFear);
        if (this.fear < 0) {retVal = false;}

        this.fear = Math.max(this.fear, 0);

        return retVal;
    }

    public float ratio() {
        return maxFear <= 0 ? 0 : fear / maxFear;
    }

    public boolean consume(float amount) {
        if (amount <= 0) return true;
        if (fear < amount) return false;
        fear -= amount;
        return true;
    }

    public void loadNBTData(CompoundNBT data) {
        data.putFloat("fear", fear);
        data.putFloat("maxFear", maxFear);
        data.putFloat("fearCostPerTick", fearCostPerTick);
    }

    public void saveNBTData(CompoundNBT data) {
        this.fear = data.getFloat("fear");
        this.maxFear = data.getFloat("maxFear");
        this.fearCostPerTick = data.getFloat("fearCostPerTick");
    }
}
