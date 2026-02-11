package com.inza.demonaddon.action.domain.beans;
import java.util.UUID;

import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class DomainInstance {

    public final Vector3d center;
    public final long startTick;
    public final int durationTicks;
    public final int keepTicks;
    public final int closeTicks;
    public final float maxRadius;
    public final UUID ownerUuid;

    public boolean forcedClosing = false;
    public long closeStartTick = -1L;
    public float closeStartRadius = 0F;

    public DomainInstance(Vector3d center, long startTick,
                          int durationTicks, float maxRadius,
                          int keepTicks, int closeTicks,
                          UUID ownerUuid) {
        this.center = center;
        this.startTick = startTick;
        this.durationTicks = durationTicks;
        this.maxRadius = maxRadius;
        this.keepTicks = keepTicks;
        this.closeTicks = closeTicks;
        this.ownerUuid = ownerUuid;
    }

    public void forceClose(long nowTick) {
        if (forcedClosing) return;
        float radiusNow = currentRadius(nowTick);
        forcedClosing = true;
        closeStartTick = nowTick;
        closeStartRadius = Math.max(0F, radiusNow);
    }

    public boolean isExpired(long nowTick) {
        if (forcedClosing) {
            boolean ret = closeTicks <= 0 || nowTick >= closeStartTick + (long) closeTicks;
            System.out.println("closing " + ret);
            return ret;
        }
        long totalEnd = startTick + (long) durationTicks + (long) keepTicks + (long) closeTicks;
        return nowTick >= totalEnd;
    }

    public float currentRadius(float nowTick) {
        if (forcedClosing) {
            return closeRadius(nowTick, closeStartTick, closeStartRadius);
        }

        float elapsed = nowTick - startTick;
        if (elapsed < 0) elapsed = 0;

        // expand
        if (elapsed < (long) durationTicks) {
            if (durationTicks <= 0) return maxRadius;
            float t = (float) elapsed / (float) durationTicks;
            // easeOutCubic: 1 - (1 - t)^3
            float eased = 1.0F - (float) Math.pow(1.0F - t, 3.0);
            return maxRadius * eased;
        }

        // maintain
        if (elapsed < (long) durationTicks + (long) keepTicks) {
            return maxRadius;
        }

        // close
        long normalCloseStart = startTick + (long) durationTicks + (long) keepTicks;
        return closeRadius(nowTick, normalCloseStart, maxRadius);
    }

    public float currentAlphaFactor(float nowTick) {
        if (forcedClosing) {
            return 1F - closeProgress(nowTick, closeStartTick);
        }

        float elapsed = nowTick - startTick;
        if (elapsed < 0) elapsed = 0;

        // expand: fade in
        if (elapsed < (long) durationTicks) {
            if (durationTicks <= 0) return 1F;
            float t = (float) elapsed / (float) durationTicks;
            return 1.0F - (float) Math.pow(1.0F - t, 3.0);
        }

        // maintain: keep fully visible
        if (elapsed < (long) durationTicks + (long) keepTicks) {
            return 1F;
        }

        // close: fade out
        long normalCloseStart = startTick + (long) durationTicks + (long) keepTicks;
        return 1F - closeProgress(nowTick, normalCloseStart);
    }

    private float closeRadius(float nowTick, float startCloseTick, float startRadius) {
        if (closeTicks <= 0) return 0F;
        float s = closeProgress(nowTick, startCloseTick);
        return startRadius * (1F - s);
    }

    private float closeProgress(float nowTick, float startCloseTick) {
        if (closeTicks <= 0) return 1F;
        float t = (float) (nowTick - startCloseTick) / (float) closeTicks; // 0..1
        t = MathHelper.clamp(t, 0F, 1F);
        // smootherstep
        return t * t * t * (t * (t * 6F - 15F) + 10F);
    }

    public long usedTicks(long nowTick) {
        long endOfUse = forcedClosing ? closeStartTick : Math.min(nowTick, startTick + (long) durationTicks + (long) keepTicks);
        long used = endOfUse - startTick;
        if (used < 0) used = 0;

        long maxUse = (long) durationTicks + (long) keepTicks;
        if (used > maxUse) used = maxUse;

        return used;
    }

}
