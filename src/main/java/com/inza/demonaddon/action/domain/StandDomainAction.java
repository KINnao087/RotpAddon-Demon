package com.inza.demonaddon.action.domain;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.config.ActionConfigField;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.inza.demonaddon.action.domain.network.packet.S2CAddDomainPacket;
import com.inza.demonaddon.init.InitEffects;
import com.inza.demonaddon.init.InitStands;
import com.inza.demonaddon.AddonNetwork;
import com.inza.demonaddon.utils.ServerUtils;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class StandDomainAction extends StandEntityAction {
    public static final int EXPAND_TICK = 10;
    public static final int CLOSE_TICK = 5;

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

    @ActionConfigField private final float fearCostPerTick;


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

        this.fearCostPerTick = builder.fearCostPerTick;
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

    @Override
    public float getStaminaCostTicking(IStandPower power) {
        return this.staminaCostPerTick * power.getMaxStamina();
    }

    public float getFearCostPerTick() {return this.fearCostPerTick;}

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) {
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
//        spawnDomainBurst(world, pos, inst.maxRadius);

        AddonNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> user),
                new S2CAddDomainPacket(inst)
        );

        int durationTicks = getDomainMaxTicks(userPower);
        userPower.setCooldownTimer(this, durationTicks);
//        MyUtils.getFearCap(user).ifPresent(fear -> {
//            fear.addFearCostPerTick(getFearCostPerTick());
//        });
    }

    public static void onActionClose(int realCd, IStandPower power) {
        power.setCooldownTimer((StandDomainAction) InitStands.DEMON_STAND_DOMAIN.get(), realCd);
//        MyUtils.getFearCap(power.getUser()).ifPresent(fear -> {
//            fear.subFearCostPerTick(InitStands.DEMON_STAND_DOMAIN.get().getFearCostPerTick());
//        });
    }

    private static final int EFFECT_DURATION = 50;
    private static final int DAMAGE_INTERVAL_TICKS = 20;
    private static final float DAMAGE_AMOUNT = 2.0F;
    public static void onDomain(World world, Vector3d center, float r, LivingEntity caster) {
        if (world.isClientSide()) {return;}
        AxisAlignedBB box = new AxisAlignedBB(center, center).inflate(r);

        List<LivingEntity> entityList = world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive());

        boolean doDamage = (world.getGameTime() % DAMAGE_INTERVAL_TICKS) == 0;

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
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.HUNGER, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.BLINDNESS, EFFECT_DURATION, 0));
            if (doDamage) {
                entity.hurt(ServerUtils.getCasterDamageSource(caster), DAMAGE_AMOUNT);
                ServerUtils.randomAddEffects(
                        entity,
                        new EffectInstance(InitEffects.HORRIFIED.get(), InitEffects.MAX_EFFECT_DURATION
                                , 0, false, false, false),
                        1.0f, InitEffects.MAX_EFFECT_STACKS
                );
            }
        }
    }



    @Override
    protected boolean standKeepsTarget(ActionTarget target) {
        return true;
    }

    private static void spawnDomainBurst(World world, Vector3d center, float radius) {
        if (!(world instanceof ServerWorld)) return;
        ServerWorld sw = (ServerWorld) world;
        RedstoneParticleData redDust = new RedstoneParticleData(1.0F, 0.0F, 0.0F, 1.2F);

        // 粒子从中心点生成，然后按速度向四周爆散。
        int count = Math.max(120, (int) (radius * 10.0f));
        double spawnY = center.y + 1.0;

        for (int i = 0; i < count; i++) {
            double dx = world.random.nextGaussian();
            double dy = world.random.nextGaussian() * 0.75 + 0.15; // 略向上扬
            double dz = world.random.nextGaussian();
            double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (len < 1.0e-4) continue;

            dx /= len;
            dy /= len;
            dz /= len;

            // TNT-like burst: high initial velocity, large outward travel.
            double speed = 1.15 + world.random.nextDouble() * 1.45;
            if (world.random.nextFloat() < 0.18f) {
                speed *= 1.25; // 少量更高速碎片
            }

            // 所有粒子从同一中心点爆开，靠速度向外散
            double px = center.x;
            double py = spawnY;
            double pz = center.z;

            // count=0 时，dx/dy/dz 会作为定向速度传到客户端，轨迹可控。
            sw.sendParticles(
                    redDust,
                    px, py, pz,
                    0,
                    dx * speed, dy * speed, dz * speed,
                    1.0
            );

            // 少量烟花粒子做炸裂感（这个粒子本身颜色不可控）。
            if (world.random.nextFloat() < 0.25f) {
                double spark = speed * (0.9 + world.random.nextDouble() * 0.5);
                sw.sendParticles(
                        ParticleTypes.FIREWORK,
                        px, py, pz,
                        1,
                        dx * spark, dy * spark, dz * spark,
                        0.0
                );
            }
        }
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
        private float fearCostPerTick = 0.01f;

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

        public Builder fearCostPerTick(float amount) {
            fearCostPerTick = amount;
            return getThis();
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }
}
