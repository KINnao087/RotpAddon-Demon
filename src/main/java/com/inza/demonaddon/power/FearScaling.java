package com.inza.demonaddon.power;

import com.inza.demonaddon.power.FearPower;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.entity.LivingEntity;

/**
 * Central place for Fear -> multiplier curves.
 * Safe by design: null checks + clamp.
 */
public final class FearScaling {
    public static final float maxLine = 200f;

    private FearScaling() {}

    public static double computeMultiplier(LivingEntity user, Stat stat) {
        double t = getFearRatioSafe(user, maxLine); // 0..1

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

    private static double getFearRatioSafe(LivingEntity user, float maxLine) {
        try {
            FearPower fear = MyUtils.getFearPower(user);
            if (fear == null) return 0.0;

            float max = maxLine;
            float cur = fear.getFear();
            if (max <= 1e-6f) return 0.0;

            double t = cur / max;
            if (!Double.isFinite(t)) return 0.0;

            return clamp01(t);
        } catch (Throwable ignored) {
            return 0.0;
        }
    }

    private static double lerp(double base, double max, double t) {
        if (t <= 1) return base + (max - base) * t;
        return max;
    }

    private static double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}