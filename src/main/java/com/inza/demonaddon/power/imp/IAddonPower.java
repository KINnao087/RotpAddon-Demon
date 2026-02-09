package com.inza.demonaddon.power.imp;

public interface IAddonPower {
    float get();
    void set(float value);

    float getMax();
    void setMax(float max);

    /** Adds value (implementation should clamp to [0, max]). */
    default void add(float amount) {
        set(get() + amount);
    }

    /**
     * Tries to consume value.
     * @return true if there was enough power and it was consumed.
     */
    default boolean consume(float amount) {
        if (amount <= 0f) return true;
        if (get() < amount) return false;
        set(get() - amount);
        return true;
    }

    /** @return ratio in [0, 1] (safe, handles max<=0). */
    default float ratio() {
        float m = getMax();
        return m <= 0f ? 0f : (get() / m);
    }

    /** @return true if current value is at least amount. */
    default boolean has(float amount) {
        return get() >= amount;
    }
}
