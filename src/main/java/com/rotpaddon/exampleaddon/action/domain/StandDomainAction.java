package com.rotpaddon.exampleaddon.action.domain;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.config.ActionConfigField;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.rotpaddon.exampleaddon.action.domain.beans.DomainInstance;
import com.rotpaddon.exampleaddon.action.domain.network.DomainNetwork;
import com.rotpaddon.exampleaddon.action.domain.network.packet.S2CAddDomainPacket;

import com.rotpaddon.exampleaddon.init.InitSounds;
import com.rotpaddon.exampleaddon.utils.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class StandDomainAction extends StandEntityAction {
    private static final int EXPAND_TICK = 10;
    private static final int CLOSE_TICK = 5;

    @ActionConfigField private final int domainMaxTicks;
    @ActionConfigField private final int domainMaxTicksZombie;
    @ActionConfigField private final int domainMaxTicksVampire;
    @ActionConfigField private final int domainMaxTicksPillarman;

    @ActionConfigField private final float domainLearningPerTick;
    @ActionConfigField private final float domainDecayPerDay;
    @ActionConfigField private final float domainCooldownPerTick;

    @ActionConfigField private final float domainMaxRadius;
    @ActionConfigField private final float domainMaxRadiusZombie;
    @ActionConfigField private final float domainMaxRadiusVampire;
    @ActionConfigField private final float domainMaxRadiusPillarman;

    @ActionConfigField private final float staminaCost;
    @ActionConfigField private final float staminaCostPerTick;


    public StandDomainAction(StandDomainAction.Builder builder) {
        super(builder);

        this.domainMaxTicks = builder.domainMaxTicks;
        this.domainMaxTicksZombie = builder.domainMaxTicksZombie;
        this.domainMaxTicksVampire = builder.domainMaxTicksVampire;
        this.domainMaxTicksPillarman = builder.domainMaxTicksPillarman;

        this.domainLearningPerTick = builder.domainLearningPerTick;
        this.domainDecayPerDay = builder.domainDecayPerDay;
        this.domainCooldownPerTick = builder.domainCooldownPerTick;

        this.domainMaxRadius = builder.domainMaxRadius;
        this.domainMaxRadiusZombie = builder.domainMaxRadiusZombie;
        this.domainMaxRadiusVampire = builder.domainMaxRadiusVampire;
        this.domainMaxRadiusPillarman = builder.domainMaxRadiusPillarman;

        this.staminaCost = builder.staminaCost;
        this.staminaCostPerTick = builder.staminaCostPerTick;
    }

    public float getDomainCooldownPerTick() {
        return this.domainCooldownPerTick;
    }

    public int getDomainMaxTicks(IStandPower standPower) {
        LivingEntity e = standPower.getUser();
        if (vampireDomain(e)) return this.domainMaxTicksVampire;
        if (pillarmanDomain(e)) return this.domainMaxTicksPillarman;
        if (zombieDomain(e)) return this.domainMaxTicksZombie;
        return this.domainMaxTicks + EXPAND_TICK + CLOSE_TICK;
    }
    public float getDomainMaxRadius(IStandPower standPower) {
        LivingEntity e = standPower.getUser();
        if (vampireDomain(e)) return this.domainMaxRadiusVampire;
        if (pillarmanDomain(e)) return this.domainMaxRadiusPillarman;
        if (zombieDomain(e)) return this.domainMaxRadiusZombie;
        return this.domainMaxRadius;
    }

    public static boolean vampireDomain(LivingEntity entity) {
        return ModPowers.VAMPIRISM.get().isHighOnBlood(entity);
    }
    public static boolean pillarmanDomain(LivingEntity entity) {
        return ModPowers.PILLAR_MAN.get().isHighLifeForce(entity);
    }
    public static boolean zombieDomain(LivingEntity entity) {
        return ModPowers.ZOMBIE.get().isHighSaturation(entity);
    }

    @Override
    public float getStaminaCost(IStandPower power) {
        return this.staminaCost;
    }

    public float getStaminaCostPerTick(IStandPower power) {
        return this.staminaCostPerTick * power.getMaxStamina();
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) {
            ClientUtils.playSound(InitSounds.DEMON_STAND_START_DOMAIN.get(), 1.0f);
            return;
        }

        LivingEntity user = userPower.getUser();
        Vector3d pos = user.position();
//        RayTraceResult result = JojoModUtil.rayTrace(user, 100, e -> e != standEntity);
//        Vector3d pos = result.getLocation();
        if (pos == null) {
            return;
        }

        long nowTick = world.getGameTime();

        DomainInstance inst = new DomainInstance(pos,
                nowTick,
                EXPAND_TICK,
                getDomainMaxRadius(userPower),
                getDomainMaxTicks(userPower),
                CLOSE_TICK,
                user.getUUID());

        DomainServerManager.addDomain(inst);

        DomainNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> user),
                new S2CAddDomainPacket(inst)
        );

        int durationTicks = getDomainMaxTicks(userPower);
        userPower.setCooldownTimer(this, durationTicks);
    }

    private static final int EFFECT_DURATION = 50;
    public static void handleDomainEffects(World world, Vector3d center, float r, LivingEntity caster) {
        if (world.isClientSide()) {return;}
        AxisAlignedBB box = new AxisAlignedBB(center, center).inflate(r);

        List<LivingEntity> entityList = world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive());


        for (LivingEntity entity : entityList) {
            if(entity.equals(caster)) {
                entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, EFFECT_DURATION, 3));
                entity.addEffect(new EffectInstance(Effects.DIG_SPEED, EFFECT_DURATION, 3));
                entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, EFFECT_DURATION, 3));
                entity.addEffect(new EffectInstance(Effects.REGENERATION, EFFECT_DURATION, 3));
                entity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, EFFECT_DURATION, 3));
                entity.addEffect(new EffectInstance(Effects.JUMP, EFFECT_DURATION, 3));
                entity.addEffect(new EffectInstance(Effects.NIGHT_VISION, 201, 0));
                entity.addEffect(new EffectInstance(Effects.SATURATION, EFFECT_DURATION, 0));
                continue;
            }
            entity.addEffect(new EffectInstance(Effects.WITHER, EFFECT_DURATION, 1));
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.HUNGER, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.BLINDNESS, EFFECT_DURATION, 0));
        }
    }

    @Override
    protected boolean standKeepsTarget(ActionTarget target) {
        return true;
    }

    public static class Builder extends StandEntityAction.AbstractBuilder<Builder> {
        private int domainMaxTicks = 100;
        private int domainMaxTicksZombie = 120;
        private int domainMaxTicksVampire = 180;
        private int domainMaxTicksPillarman = 180;
        private float domainLearningPerTick = 0.1F;
        private float domainDecayPerDay = 0;
        private float domainCooldownPerTick = 3;
        private float domainMaxRadius = 20;
        private float domainMaxRadiusZombie = 30;
        private float domainMaxRadiusVampire = 50;
        private float domainMaxRadiusPillarman = 50;
        private float staminaCost = 0.02f;
        private float staminaCostPerTick = 0.01f;

        private Supplier<SoundEvent> voiceLineWithStandSummoned = () -> null;
        private Supplier<SoundEvent> domainSound = () -> null;
        private Supplier<SoundEvent> domainCloseSound = () -> null;

        public Builder staminaCost(float staminaCost) {
            this.staminaCost = staminaCost;
            return getThis();
        }

        public Builder staminaCostPerTick(float staminaCostPerTick) {
            this.staminaCostPerTick = staminaCostPerTick;
            return getThis();
        }

        public Builder domainMaxTicks(int forHuman, int forVampire, int forPillarman, int forZombie) {
            forHuman = Math.max(0, forHuman);
            forZombie = Math.max(forHuman, forZombie);
            forVampire = Math.max(forHuman, forVampire);
            forPillarman = Math.max(forHuman, forPillarman);
            this.domainMaxTicks = forHuman;
            this.domainMaxTicksZombie = forZombie;
            this.domainMaxTicksVampire = forVampire;
            this.domainMaxTicksPillarman = forVampire;
            return getThis();
        }

        public Builder getDomainMaxRadius(int forHuman, int forVampire, int forPillarman, int forZombie) {
            forHuman = Math.max(0, forHuman);
            forZombie = Math.max(forHuman, forZombie);
            forVampire = Math.max(forHuman, forVampire);
            forPillarman = Math.max(forHuman, forPillarman);
            this.domainMaxRadius = forHuman;
            this.domainMaxRadiusZombie = forZombie;
            this.domainMaxRadiusVampire = forVampire;
            this.domainMaxRadiusPillarman = forVampire;

            return getThis();
        }

        public Builder domainMaxTicks(int forHuman, int forVampire) {
            return domainMaxTicks(forHuman, forVampire, forVampire, forVampire);
        }

        public Builder domainLearningPerTick(float points) {
            this.domainLearningPerTick = points;
            return getThis();
        }

        public Builder domainDecayPerDay(float points) {
            this.domainDecayPerDay = points;
            return getThis();
        }

        public Builder domainCooldownPerTick(float ticks) {
            this.domainCooldownPerTick = ticks;
            return getThis();
        }

        public Builder voiceLineWithStandSummoned(Supplier<SoundEvent> voiceLine) {
            this.voiceLineWithStandSummoned = voiceLine;
            return getThis();
        }

        public Builder timeStopSound(Supplier<SoundEvent> sound) {
            this.domainSound = sound;
            return getThis();
        }

        public Builder domainCloseSound(Supplier<SoundEvent> sound) {
            this.domainCloseSound = sound;
            return getThis();
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }
}
