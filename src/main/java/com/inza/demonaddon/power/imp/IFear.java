package com.inza.demonaddon.power.imp;

public interface IFear extends IAddonPower{
    /** Last tick when fear was gained (useful for rate limiting / decay). */
//    long getLastGainTick();
//    void setLastGainTick(long tick);

    /** Fear decay speed (per second). 0 = no decay. */
    float getDecayPerSecond();
    void setDecayPerSecond(float v);

    // Convenience naming (readability)
    default float getFear() { return get(); }
    default float getMaxFear() { return getMax(); }

    default void setFear(float v) { set(v); }
    default void setMaxFear(float v) { setMax(v); }
}
