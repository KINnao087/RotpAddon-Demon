package com.inza.demonaddon.action.freeze.client;

import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.action.domain.DomainInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = AddonMain.MOD_ID)
public class FreezeClientState {

    private FreezeClientState() {}

    public static final List<DomainInstance> DOMAINS = new ArrayList<>();

    public static void addOrReplace(DomainInstance inst) {
        System.out.println("addOrReplace: " + inst);
        for (int i = 0; i < DOMAINS.size(); i++) {
            DomainInstance d = DOMAINS.get(i);
            if (d.ownerUuid.equals(inst.ownerUuid) && d.startTick == inst.startTick) {
                DOMAINS.set(i, inst);
                return;
            }
        }
        DOMAINS.add(inst);
    }

    public static void forceCloseByOwner(UUID owner, long nowTick) {
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

    // ====== 防抖：inDomain 稳定判定（滞回）======
    private static boolean stableInDomain = false;
    private static final double HYS = 0.6; // 滞回厚度（方块），0.3~1.0 自己调

    private static boolean isInDomainStable(Vector3d pos, long tick) {
        // 没领域直接 false，并顺手复位
        if (DOMAINS.isEmpty()) {
            stableInDomain = false;
            return false;
        }

        // 只要有一个领域满足就算在（带滞回）
        for (DomainInstance d : DOMAINS) {
            double r = (double) d.currentRadius(tick);
            if (r <= 0.001) continue;

            double rin = Math.max(0.0, r - HYS);
            double rout = r + HYS;

            double d2 = pos.distanceToSqr(d.center);
            double thresh = stableInDomain ? (rout * rout) : (rin * rin);

            if (d2 <= thresh) {
                stableInDomain = true;
                return true;
            }
        }

        stableInDomain = false;
        return false;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        boolean inFreeze = true /* 你自己的 stableInDomain / inFreeze 判定 */;
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;

        long nowTick = mc.level.getGameTime();
        purgeExpired(nowTick);
        if (DOMAINS.isEmpty()) return;

        Vector3d camPos = mc.gameRenderer.getMainCamera().getPosition();

        float partial = event.getPartialTicks();
        float nowTickF = (float) nowTick + partial;

        for (DomainInstance d : DOMAINS) {
            float r = d.currentRadius(nowTickF);
            if (r <= 0.001F) continue;

            // 反色球（覆盖区域反色）
            FreezeRenderer.renderInvertSphere(
                    event.getMatrixStack(),
                    camPos,
                    d.center,
                    r
            );
        }
    }
}
