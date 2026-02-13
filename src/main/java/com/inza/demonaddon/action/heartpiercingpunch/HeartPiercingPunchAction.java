package com.inza.demonaddon.action.heartpiercingpunch;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandBlockPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.action.stand.punch.StandMissedPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;

import com.inza.demonaddon.init.InitEffects;
import com.inza.demonaddon.utils.ServerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;


public class HeartPiercingPunchAction extends StandEntityHeavyAttack {

    public HeartPiercingPunchAction(HeartPiercingPunchAction.Builder builder) {
        super(builder);
    }

    @Override
    public int getStandWindupTicks(IStandPower standPower, StandEntity standEntity) {
        int base = super.getStandWindupTicks(standPower, standEntity);
        return Math.max(1, (int) Math.floor(base * 0.75));
    }

    @Override
    public int getStandRecoveryTicks(IStandPower standPower, StandEntity standEntity) {
        int base = super.getStandRecoveryTicks(standPower, standEntity);
        return Math.max(1, (int) Math.ceil(base * 1.15));
    }

    @Override
    public void onTaskSet(net.minecraft.world.World world, StandEntity standEntity, IStandPower standPower,
                          Phase phase, StandEntityTask task, int ticks) {
        super.onTaskSet(world, standEntity, standPower, phase, task, ticks);
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        StandEntityPunch punch = super.punchEntity(stand, target, dmgSource);
        punch.damage(punch.getDamage() * 1.5F);
        punch.reduceKnockback(0.0F);

        if (!(target instanceof LivingEntity)) return punch;

        ServerUtils.randomAddEffects(
                (LivingEntity) target,
                new EffectInstance(InitEffects.HORRIFIED.get(), InitEffects.MAX_EFFECT_DURATION
                        , 2, false, false, false),
                1.0f, InitEffects.MAX_EFFECT_STACKS
        );

        LivingEntity victim = (LivingEntity) target;
        victim.addEffect(new EffectInstance(Effects.WITHER, 40, 0, false, false, false));
        victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 5,false, false, false));
        victim.addEffect(new EffectInstance(ModStatusEffects.BLEEDING.get(), 40, 1));

        return punch;
    }


    public static class HeartPiercingPunchInstance extends StandEntityPunch {

        public HeartPiercingPunchInstance(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
            super(stand, target, dmgSource);
        }

        @Override
        protected boolean onAttack(StandEntity stand, Entity target, StandEntityDamageSource dmgSource, float damage) {
            return super.onAttack(stand, target, dmgSource, damage);
        }


        @Override
        protected void afterAttack(StandEntity stand, Entity target, StandEntityDamageSource dmgSource,
                                   StandEntityTask task, boolean hurt, boolean killed) {
            super.afterAttack(stand, target, dmgSource, task, hurt, killed);
        }

        private static float damageBonusFromStand(StandEntity stand) {
            return (float) Math.min(stand.getAttackDamage() * 0.15, 6.0);
        }
    }

    @Override
    public StandBlockPunch punchBlock(StandEntity stand, BlockPos pos, BlockState state, Direction face) {
        return super.punchBlock(stand, pos, state, face).impactSound(null);
    }

    @Override
    public StandMissedPunch punchMissed(StandEntity stand) {
        return super.punchMissed(stand);
    }

    @Override
    public SoundEvent getPunchSwingSound() {
        return super.getPunchSwingSound();
    }
}
