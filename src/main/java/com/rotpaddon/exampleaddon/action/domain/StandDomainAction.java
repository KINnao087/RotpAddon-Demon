package com.rotpaddon.exampleaddon.action.domain;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.rotpaddon.exampleaddon.action.domain.beans.DomainInstance;
import com.rotpaddon.exampleaddon.action.domain.network.DomainNetwork;
import com.rotpaddon.exampleaddon.action.domain.network.packet.S2CAddDomainPacket;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class StandDomainAction extends StandEntityAction {
    private static final int EXPAND_TICK = 50;
    private static final int KEEP_TICK = 50;
    private static final int CLOSE_TICK = 50;
    private static final float MAX_RADUIS = 80f;

    public StandDomainAction(StandEntityAction.Builder builder) {
        super(builder);
    }

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

        DomainInstance inst = new DomainInstance(pos, nowTick, EXPAND_TICK, MAX_RADUIS, KEEP_TICK, CLOSE_TICK, user.getUUID());
        DomainServerManager.addDomain(inst);

        DomainNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> user),
                new S2CAddDomainPacket(inst)
        );
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
            entity.addEffect(new EffectInstance(Effects.WITHER, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.HUNGER, EFFECT_DURATION, 0));
            entity.addEffect(new EffectInstance(Effects.BLINDNESS, EFFECT_DURATION, 0));
        }
    }

    @Override
    protected boolean standKeepsTarget(ActionTarget target) {
        return true;
    }
}
