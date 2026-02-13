package com.inza.demonaddon.utils;

import com.inza.demonaddon.init.InitEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;

import java.util.Map;

public class ServerUtils {
    private static final float DEFAULT_EFFECT_PROBABILITY = 1.0F;

    public static DamageSource getCasterDamageSource(LivingEntity caster) {
        if (caster instanceof net.minecraft.entity.player.PlayerEntity) {
            return DamageSource.playerAttack((net.minecraft.entity.player.PlayerEntity) caster);
        }
        return DamageSource.mobAttack(caster);
    }

    public static boolean randomAddEffects(LivingEntity entity, EffectInstance effect
                        , float probability, int maxStacks) {
        if (entity == null || effect == null) return false;
        if (!MyUtils.randomTrue(probability)) return false;

        Map<Effect, EffectInstance> map = entity.getActiveEffectsMap();
        Effect type = effect.getEffect();
        EffectInstance inst = map.get(type);
        if (inst != null) {
            int amp = Math.min(inst.getAmplifier() + 1, maxStacks);
            int dur = Math.max(inst.getDuration(), effect.getDuration());
            return entity.addEffect(new EffectInstance(type, dur, amp
                    , effect.isAmbient(), effect.isVisible(), effect.showIcon()
            ));
        }

        return entity.addEffect(new EffectInstance(
                type,
                effect.getDuration(),
                effect.getAmplifier(),
                effect.isAmbient(),
                effect.isVisible(),
                effect.showIcon()
        ));
    }
}
