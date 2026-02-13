package com.inza.demonaddon.action.normalactions;

import com.github.standobyte.jojo.action.stand.StandEntityMeleeBarrage;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import com.inza.demonaddon.init.InitEffects;
import com.inza.demonaddon.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

public class DemonStandMeleeBarrage extends StandEntityMeleeBarrage {
    private static float probability = 0.01f;

    public DemonStandMeleeBarrage(StandEntityMeleeBarrage.Builder builder) {
        super(builder);
    }

    @Override
    public BarrageEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        BarrageEntityPunch punch = super.punchEntity(stand, target, dmgSource);

        if (!(target instanceof LivingEntity)) {return punch;}

        LivingEntity livingEntity = (LivingEntity) target;
        ServerUtils.randomAddEffects(
                livingEntity,
                new EffectInstance(InitEffects.HORRIFIED.get(), InitEffects.MAX_EFFECT_DURATION
                        , 0, false, true, true),
                probability, InitEffects.MAX_EFFECT_STACKS
        );

        return punch;
    }
}
