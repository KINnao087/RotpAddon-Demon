package com.inza.demonaddon.action.demonview;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.inza.demonaddon.action.demonview.network.packet.S2CDemonVisionPacket;
import com.inza.demonaddon.AddonNetwork;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class DemonViewAction extends StandEntityAction {
    private final float expandRadius;

    public DemonViewAction(Builder builder) {
        super((StandEntityAction.AbstractBuilder<?>) builder.holdType());
        this.expandRadius = builder.expandRadius;
    }

    @Override
    public void startedHolding(World world, LivingEntity user, IStandPower power, ActionTarget target, boolean requirementsFulfilled) {
        super.startedHolding(world, user, power, target, requirementsFulfilled);
        if (world.isClientSide() || !requirementsFulfilled || user == null) return;
        sendVisionPacket(user, true);
    }

    @Override
    public void stoppedHolding(World world, LivingEntity user, IStandPower power, int ticksHeld, boolean willFire) {
        super.stoppedHolding(world, user, power, ticksHeld, willFire);
        if (world.isClientSide() || user == null) return;
        sendVisionPacket(user, false);
    }

    private void sendVisionPacket(LivingEntity caster, boolean enabled) {
        if (!(caster instanceof ServerPlayerEntity)) return;
        AddonNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) caster),
                new S2CDemonVisionPacket(enabled, caster.getUUID(), enabled ? expandRadius : 0.0F)
        );
    }

    public static class Builder extends StandEntityAction.AbstractBuilder<Builder> {
        private float expandRadius = 100.0F;

        public Builder expandRadius(float expandRadius) {
            this.expandRadius = Math.max(0.0F, expandRadius);
            return getThis();
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }
}
