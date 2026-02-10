package com.inza.demonaddon.power;

import com.inza.demonaddon.power.FearPower;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.entity.LivingEntity;

/**
 * Central place for Fear -> multiplier curves.
 * Safe by design: null checks + clamp.
 */
public final class FearScaling {

    private FearScaling() {}

    public static double computeMultiplier(LivingEntity user, Stat stat) {
        double t = getFearRatioSafe(user); // 0..1

        // Example curve: higher fear => weaker.
        switch (stat) {
            case DAMAGE:
                return lerp(1, 1.55, t);
            case SPEED:
                return lerp(1, 1.55, t);
            case KNOCKBACK:
                return lerp(1.00, 1.00, t);
            case DURABILITY:
                return lerp(1, 1.55, t);
            case PRECISION:
                return lerp(1, 1.55, t);
            default:
                return 1.0;
        }
    }

    private static double getFearRatioSafe(LivingEntity user) {
        try {
            FearPower fear = MyUtils.getFearPower(user);
            if (fear == null) return 0.0;

            float max = fear.getMaxFear();
            float cur = fear.getFear();
            if (max <= 1e-6f) return 0.0;

            double t = cur / max;
            if (!Double.isFinite(t)) return 0.0;

            return clamp01(t);
        } catch (Throwable ignored) {
            return 0.0;
        }
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}