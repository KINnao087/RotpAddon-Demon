package com.inza.demonaddon.action.heartpiercingpunch;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandBlockPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.action.stand.punch.StandMissedPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class HeartPiercingPunchAction extends StandEntityHeavyAttack {

    public HeartPiercingPunchAction(Builder builder) {
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
        StandEntityPunch base = super.punchEntity(stand, target, dmgSource);

        double strength = stand.getAttackDamage();

        return new HeartPiercingPunchInstance(stand, target, dmgSource)
                .damage(com.github.standobyte.jojo.entity.stand.StandStatFormulas.getHeavyAttackDamage(strength))
                .addKnockback(0.65F)
                .setStandInvulTime(10)
                .impactSound(() -> getPunchSwingSound());
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
            if (!stand.level.isClientSide() && hurt && target instanceof LivingEntity) {
                LivingEntity victim = (LivingEntity) target;
                victim.addEffect(new EffectInstance(Effects.WITHER, 40, 0));
                victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20, 2));
                float bonus = Math.max(1.0F, damageBonusFromStand(stand));
                victim.hurt(dmgSource, bonus);
                victim.push(0.0D, 0.18D, 0.0D);
                victim.hurtMarked = true;
            }

            super.afterAttack(stand, target, dmgSource, task, hurt, killed);
        }

        private static float damageBonusFromStand(StandEntity stand) {
            return (float) Math.min(stand.getAttackDamage() * 0.15, 6.0);
        }
    }

    @Override
    public StandBlockPunch punchBlock(StandEntity stand, BlockPos pos, BlockState state, Direction face) {
        return super.punchBlock(stand, pos, state, face);
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
