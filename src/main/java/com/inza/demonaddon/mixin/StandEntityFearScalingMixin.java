package com.inza.demonaddon.mixin;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.inza.demonaddon.power.FearScaling;
import com.inza.demonaddon.power.Stat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Dynamically scales stand combat stats based on owner's Fear.
 *
 * IMPORTANT:
 * - Keep ONLY mixin classes in this package.
 * - Put shared logic/types (enums/helpers) OUTSIDE this package to avoid IllegalClassLoadError.
 */
@Mixin(StandEntity.class)
public class StandEntityFearScalingMixin {

    // Optional: if you only want to affect your stand, set this.
    // If you want ALL stands to be affected, set this to null and the filter will be skipped.
    private static final ResourceLocation ONLY_AFFECT_STAND_ID =
            new ResourceLocation("demonaddon", "demon_stand");

    @Inject(method = "getAttackDamage()D", at = @At("RETURN"), cancellable = true, remap = false)
    private void demonaddon$scaleAttackDamage(CallbackInfoReturnable<Double> cir) {
        apply(cir, Stat.DAMAGE);
    }

    @Inject(method = "getAttackSpeed()D", at = @At("RETURN"), cancellable = true, remap = false)
    private void demonaddon$scaleAttackSpeed(CallbackInfoReturnable<Double> cir) {
        apply(cir, Stat.SPEED);
    }

    @Inject(method = "getAttackKnockback()D", at = @At("RETURN"), cancellable = true, remap = false)
    private void demonaddon$scaleAttackKnockback(CallbackInfoReturnable<Double> cir) {
        apply(cir, Stat.KNOCKBACK);
    }

    @Inject(method = "getDurability()D", at = @At("RETURN"), cancellable = true, remap = false)
    private void demonaddon$scaleDurability(CallbackInfoReturnable<Double> cir) {
        apply(cir, Stat.DURABILITY);
    }

    @Inject(method = "getPrecision()D", at = @At("RETURN"), cancellable = true, remap = false)
    private void demonaddon$scalePrecision(CallbackInfoReturnable<Double> cir) {
        apply(cir, Stat.PRECISION);
    }

    private void apply(CallbackInfoReturnable<Double> cir, Stat stat) {
        double base = cir.getReturnValue();
        if (!Double.isFinite(base)) return;

        StandEntity stand = (StandEntity) (Object) this;

        // Optional filter: only affect one specific stand.
        if (ONLY_AFFECT_STAND_ID != null && !isStandId(stand, ONLY_AFFECT_STAND_ID)) {
            return;
        }

        LivingEntity user = stand.getUser();
        if (user == null) return;

        double mult = FearScaling.computeMultiplier(user, stat);

        // Safety clamp: prevent negative / NaN / crazy values.
        if (!Double.isFinite(mult) || mult <= 0.0) return;

        cir.setReturnValue(base * mult);
    }

    /**
     * Tries best-effort to obtain stand type registry name.
     * RotP API may differ across versions, so this method is defensive.
     */
    private static boolean isStandId(StandEntity stand, ResourceLocation targetId) {
        IStandPower power = stand.getUserPower();
        if (power == null) return false;

        StandType<?> type = power.getType();
        if (type == null) return false;

        ResourceLocation id = type.getRegistryName();
        return new ResourceLocation("demonaddon", "demon_stand").equals(id);
    }
}
