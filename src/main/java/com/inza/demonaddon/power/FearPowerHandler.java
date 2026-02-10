package com.inza.demonaddon.power;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.network.AddonNetwork;
import com.inza.demonaddon.network.packet.S2CFearSyncPacket;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;


@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class FearPowerHandler {
    @SubscribeEvent
    public static void onKillOtherEntity(final LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide()) return;

        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity)) return;

        if (attacker instanceof StandEntity) {
            LivingEntity owner = ((StandEntity) attacker).getUser();
            if (!(owner instanceof LivingEntity)) return;

            MyUtils.getFearCap(owner).ifPresent(fear -> {
                float before = fear.getFear();
                fear.addFear(5f);

                if (!MyUtils.hasMyStand(owner)) {return;}

                AddonNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) owner),
                        new S2CFearSyncPacket(fear.getFear(), fear.getMaxFear())
                );
            });
            return;
        }

//        if (!(attacker instanceof PlayerEntity)) {return;}
        if (!MyUtils.hasMyStand(attacker)) return;
        MyUtils.getFearCap((LivingEntity) attacker).ifPresent(fear -> {
            fear.addFear(5f);

            if (!(attacker instanceof ServerPlayerEntity)) {return;}

            AddonNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) attacker),
                    new S2CFearSyncPacket(fear.getFear(), fear.getMaxFear())
            );
        });
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.level.isClientSide()) return;
//        if (!(entity instanceof PlayerEntity)) return;
        if (!MyUtils.hasMyStand(entity)) return;

        MyUtils.getFearCap(entity).ifPresent(fear -> {
//            System.out.println("fearCostPertick = " + fear.getFearCostPerTick());
            fear.subFear(fear.getFearCostPerTick());
        });
    }

    @SubscribeEvent
    public static void onPlayerRenderGUITick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayerEntity)) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.player;

//         throttle: sync every 5 ticks
//        if ((player.tickCount % 5) != 0) return;

        MyUtils.getFearCap(player).ifPresent(fear -> {
            if (player.connection == null) return; // avoid login/respawn edge cases

            AddonNetwork.CHANNEL.sendTo(
                    new S2CFearSyncPacket(fear.getFear(), fear.getMaxFear()),
                    player.connection.connection,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }
}
