package com.rotpaddon.exampleaddon.action.domain;

import com.rotpaddon.exampleaddon.action.domain.beans.DomainInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

            long life = (long) d.durationTicks + d.keepTicks + d.closeTicks;
            if (nowTick - d.startTick > life) {
                it.remove();
                continue;
            }

            ServerPlayerEntity caster = world.getServer().getPlayerList().getPlayer(d.ownerUuid);
            if (caster == null) continue;

            float r = d.currentRadius(nowTick);
            if (r <= 0.1f) continue;

            StandDomainAction.handleDomainEffects(world, d.center, r, caster);
        }
    }

}

