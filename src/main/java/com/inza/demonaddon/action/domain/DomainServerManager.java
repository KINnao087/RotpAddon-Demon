package com.inza.demonaddon.action.domain;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.inza.demonaddon.action.domain.network.packet.S2CForceCloseDomainPacket;
import com.inza.demonaddon.AddonNetwork;
import com.inza.demonaddon.init.InitStands;
import com.inza.demonaddon.utils.ClientUtils;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DomainServerManager {
    private static final int REFRESH_TICK = 10;

    private static final Map<UUID, DomainInstance> DOMAINS = new ConcurrentHashMap<>();

    public static void addDomain(DomainInstance inst) {
        DOMAINS.put(inst.ownerUuid, inst);
    }
    public static void removeDomain(UUID uuid, long nowTick) {
        DomainInstance inst = DOMAINS.get(uuid);
        if (inst == null) return;
        inst.forceClose(nowTick);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        IStandPower power = IStandPower.getStandPowerOptional(player).orElse(null);
        power.setCooldownTimer(InitStands.DEMON_STAND_DOMAIN.get(), 0);
        AddonNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                new S2CForceCloseDomainPacket(player.getUUID(), ClientUtils.getNowTicks())
        );
        DomainServerManager.removeDomain(player.getUUID(), player.level.getGameTime());
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isClientSide()) return;
        if (event.phase != TickEvent.Phase.END) return;

        long nowTick = world.getGameTime();

        if (nowTick % REFRESH_TICK != 0) return;

        Iterator<Map.Entry<UUID, DomainInstance>> it = DOMAINS.entrySet().iterator();

        while (it.hasNext()) {
            DomainInstance d = it.next().getValue();

            ServerPlayerEntity caster = world.getServer().getPlayerList().getPlayer(d.ownerUuid);

            if (caster == null) continue;

            StandDomainAction open = (StandDomainAction) InitStands.DEMON_STAND_DOMAIN.get();
            IStandPower power = IStandPower.getPlayerStandPower(caster);

            if (power == null) continue;

            long life = (long) d.durationTicks + d.keepTicks + d.closeTicks;
            if (nowTick - d.startTick > life || d.isExpired(nowTick)) {
                if (d.isExpired(nowTick)) {
                    System.out.println("domain force closed");
                }
                long usedTick = d.usedTicks(nowTick);

                long used = d.usedTicks(nowTick);
                int realCd = (int) Math.ceil(used * open.getDomainCooldownPerTick());

                AddonNetwork.CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> caster),
                        new S2CForceCloseDomainPacket(caster.getUUID(), nowTick)
                );

                if (caster.isCreative()) realCd = 0;

                StandDomainAction.onActionClose(realCd, power);


                it.remove();
                continue;
            }

            float r = d.currentRadius(nowTick);
            if (r <= 0.1f) continue;

            StandDomainAction.onDomain(world, d.center, r, caster);
            if (!MyUtils.consumeFearPower(caster, InitStands.DEMON_STAND_DOMAIN.get().getFearCostPerTick())) {
                LivingEntity user = power.getUser();
                AddonNetwork.CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> user),
                        new S2CForceCloseDomainPacket(user.getUUID(), nowTick)
                );

                long used = d.usedTicks(nowTick);
                int realCd = (int) Math.ceil(used * open.getDomainCooldownPerTick());

                if (caster.isCreative()) realCd = 0;
                StandDomainAction.onActionClose(realCd, power);
                it.remove();
            }
        }
    }

}

