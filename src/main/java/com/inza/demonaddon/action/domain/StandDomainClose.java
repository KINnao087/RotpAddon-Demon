package com.inza.demonaddon.action.domain;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.inza.demonaddon.network.AddonNetwork;
import com.inza.demonaddon.network.packet.S2CForceCloseDomainPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class StandDomainClose extends StandEntityAction {
    public StandDomainClose(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) {
            return;
        }

        LivingEntity user = userPower.getUser();

        long nowTick = world.getGameTime();

        AddonNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> user),
                new S2CForceCloseDomainPacket(user.getUUID(), nowTick)
        );
        DomainServerManager.removeDomain(user.getUUID(), nowTick);
    }
}
