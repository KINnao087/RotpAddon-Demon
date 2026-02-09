package com.inza.demonaddon.action.domain.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.action.domain.beans.DomainInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.FORGE, modid = AddonMain.MOD_ID)
public final class DomainClientState {

    private DomainClientState() {}

    public static final List<DomainInstance> DOMAINS = new ArrayList<>();

    public static void addOrReplace(DomainInstance inst) {
        for (int i = 0; i < DOMAINS.size(); i++) {
            DomainInstance d = DOMAINS.get(i);
            if (d.ownerUuid.equals(inst.ownerUuid) && d.startTick == inst.startTick) {
                DOMAINS.set(i, inst);
                return;
            }
        }
        DOMAINS.add(inst);
    }

    public static void forceCloseByOwner(java.util.UUID owner, long nowTick) {
        for (DomainInstance d : DOMAINS) {
            if (d.ownerUuid.equals(owner)) {
                d.forceClose(nowTick);
            }
        }
    }

    private static void purgeExpired(long nowTick) {
        Iterator<DomainInstance> it = DOMAINS.iterator();
        while (it.hasNext()) {
            DomainInstance d = it.next();
            if (d.isExpired(nowTick)) {
                it.remove();
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null || mc.player == null) return;


        ClientPlayerEntity player = mc.player;
        boolean inDomain = isInDomain(player.position(), mc.level.getGameTime());
//        if (inDomain) LogManager.getLogger().info("here   tick DomainShaders");
        DomainShader.tick(mc, inDomain);
    }

    private static boolean isInDomain(Vector3d pos, long tick) {
        for (DomainInstance d : DOMAINS) {
            double r = (double)d.currentRadius(tick);
            double dist = pos.distanceTo(d.center);
            if (dist < r) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        long nowTick = mc.level.getGameTime();
        purgeExpired(nowTick);
        if (DOMAINS.isEmpty()) return;

        Vector3d camPos = mc.gameRenderer.getMainCamera().getPosition();

        // partial tick
        float partial = event.getPartialTicks();
        float nowTickF = (float) nowTick + partial;

        for (DomainInstance d : DOMAINS) {
            float r = d.currentRadius(nowTickF);
            if (r <= 0.001F) continue;

//             black ball
            DomainRenderer.renderSolidSphere(event.getMatrixStack(), camPos, d.center, r - 0.1f,
                    255, 0, 0, 255);
            DomainRenderer.renderSolidSphere(event.getMatrixStack(), camPos, d.center, r,
                    0, 0, 0, 255);

        }
    }
}
