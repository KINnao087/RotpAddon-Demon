package com.rotpaddon.exampleaddon.action.freeze;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import com.rotpaddon.exampleaddon.action.domain.beans.DomainInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Freeze field instance (1.16.5).
 *
 * Similar to TimeStopInstance:
 * - holds duration
 * - holds area
 * - optional user & stamina ticking
 * - optional status effect instance tracking
 * - optional resume sounds
 *
 * Range unit: BLOCKS (radiusBlocks).
 * Shape: circle on XZ (2D). If you want sphere, check contains3D().
 */
public class FreezeInstance {

    // ---- id generation ----
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    private final World world;
    private final int id;

    private final int startingTicks;
    private int ticksLeft;
    private int ticksPassed;

    // ---- area ----
    private DomainInstance area;

    // ---- owner / stamina ----
    @Nullable public final LivingEntity user;
    private final LazyOptional<IStandPower> userPower;

    /**
     * Your freeze "action" logic interface.
     * Make your Freeze skill / action implement this (or create an adapter).
     */
    @Nullable public final IFreezeAction action;

    // ---- optional status effect tracking ----
    @Nullable private EffectInstance statusEffectInstance;

    // ---- optional sounds ----
    @Nullable private SoundEvent freezeResumeSound;
    @Nullable private SoundEvent freezeResumeVoiceLine;
    @Nullable private SoundEvent freezeManualResumeVoiceLine;

    // ---- flags ----
    private boolean ticksManuallySet = false;
    private boolean alwaysSayVoiceLine = false;

    // ---- sound timing (copy the pattern from TimeStopInstance) ----
    public static final int RESUME_SOUND_TICKS = 10;
    public static final int RESUME_VOICELINE_TICKS = 30;


    public FreezeInstance(
            World world,
            int ticks,
            Vector3d center,
            float radiusBlocks,
            long startTick,
            int durationTicks,
            int keepTicks,
            int closeTicks,
            @Nullable LivingEntity user,
            @Nullable IFreezeAction action
    ) {
        this(world, ticks, center, radiusBlocks, startTick, durationTicks, keepTicks, closeTicks, user, action, NEXT_ID.getAndIncrement());
    }

    public FreezeInstance(
            World world,
            int ticks,
            Vector3d center,
            float radiusBlocks,
            long startTick,
            int durationTicks,
            int keepTicks,
            int closeTicks,
            @Nullable LivingEntity user,
            @Nullable IFreezeAction action,
            int id
    ) {
        this.world = world;
        this.startingTicks = ticks;
        this.ticksLeft = ticks;

//        this.center = center;
//        this.radiusBlocks = radiusBlocks;
        area = new DomainInstance(center, startTick, durationTicks, (float)radiusBlocks, keepTicks, closeTicks, user.getUUID());
        this.user = user;
        this.userPower = user != null ? IStandPower.getStandPowerOptional(user) : LazyOptional.empty();
        this.action = action;
        this.id = id;
    }

    // ---------------- API ----------------

    public int getId() { return id; }

    public int getStartingTicks() { return startingTicks; }

    public int getTicksLeft() { return ticksLeft; }

    public int getTicksPassed() { return ticksPassed; }

    public Vector3d getCenter() { return area.center; }

    public double getRadiusBlocks(float nowTick) { return area.currentRadius(nowTick);}

    public void setSounds(@Nullable SoundEvent resumeSound,
                          @Nullable SoundEvent resumeVoiceLine,
                          @Nullable SoundEvent manualResumeVoiceLine) {
        this.freezeResumeSound = resumeSound;
        this.freezeResumeVoiceLine = resumeVoiceLine;
        this.freezeManualResumeVoiceLine = manualResumeVoiceLine;
    }

    public void setStatusEffectInstance(@Nullable EffectInstance effectInstance) {
        this.statusEffectInstance = effectInstance;
    }

    @Nullable
    public EffectInstance getStatusEffectInstance() {
        return statusEffectInstance;
    }

    /**
     * Main ticking method.
     * @return true if this instance should be removed (ended)
     */
    public boolean tick() {
        ticksPassed++;
        ticksLeft--;

        // Mirror TimeStopInstance behavior: client keeps only "your own" instance ticking.
        if (world.isClientSide() && user != ClientUtil.getClientPlayer()) {
            return false;
        }

        // user-driven stamina ticking & early termination
        if (user != null) {
            if (!user.isAlive()) {
                return true;
            }

            boolean shouldEnd = userPower.map(power -> {
                if (!power.hasPower()) {
                    return true;
                }

                // action gating
                if (action != null && !action.isUnlocked(power)) {
                    return true;
                }

                // stamina ticking
                float staminaCost = power.getStaminaTickGain();
                if (action != null) {
                    staminaCost += action.getStaminaCostTicking(power);
                }

                if (!power.consumeStamina(staminaCost, true)) {
                    power.setStamina(0);
                    return true;
                }

                return false;
            }).orElse(false);

            if (shouldEnd) {
                return true;
            }

            tickSounds();
        }

        return ticksLeft <= 0;
    }

    /**
     * Manually set remaining ticks (e.g. manual resume).
     * Handler/network sync should be done outside (in your FreezeHandler).
     */
    public void setTicksLeft(int ticks) {
        if (!ticksManuallySet && ticksLeft > RESUME_VOICELINE_TICKS && ticks < RESUME_VOICELINE_TICKS) {
            alwaysSayVoiceLine = true;
        }
        this.ticksLeft = ticks;
        this.ticksManuallySet = true;
    }

    public boolean wereTicksManuallySet() {
        return ticksManuallySet;
    }

    // ---------------- Range checks ----------------

    /** Circle range on XZ. */
    public boolean inRange(BlockPos pos, float nowTick) {
        return inRange(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, nowTick);
    }

    /** Circle range on XZ (2D). */
    public boolean inRange(double x, double y, double z, float nowTick) {
        double dx = x - getCenter().x;
        double dz = z - getCenter().z;
        return dx * dx + dz * dz <= getRadiusBlocks(nowTick) * getRadiusBlocks(nowTick);
    }

    /** True if still active AND pos is in range. */
    public boolean isFrozenAt(BlockPos pos, float nowTick) {
        return ticksLeft > 0 && inRange(pos, nowTick);
    }

    /** Sphere range (3D) if you want it later. */
    public boolean inRange3D(double x, double y, double z, float nowTick) {
        double dx = x - getCenter().x;
        double dy = y - getCenter().y;
        double dz = z - getCenter().z;
        return dx * dx + dy * dy + dz * dz <= getRadiusBlocks(nowTick) * getRadiusBlocks(nowTick);
    }

    // ---------------- Sounds ----------------

    private void tickSounds() {
        // 这里不直接发包/播声 —— 你放到 FreezeHandler 里做（更像原版 TimeStopHandler 的结构）
        // 我只负责决定“什么时候该播”。

        if (ticksLeft == RESUME_SOUND_TICKS) {
            // handler: if (instance.getFreezeResumeSound()!=null) broadcast it to nearby players
        }
        else if (ticksLeft == RESUME_VOICELINE_TICKS || alwaysSayVoiceLine) {
            // handler: pick voiceLine = ticksManuallySet ? freezeManualResumeVoiceLine : freezeResumeVoiceLine
            alwaysSayVoiceLine = false;
        }
    }

    @Nullable
    public SoundEvent getFreezeResumeSound() { return freezeResumeSound; }

    @Nullable
    public SoundEvent getFreezeResumeVoiceLine() { return freezeResumeVoiceLine; }

    @Nullable
    public SoundEvent getFreezeManualResumeVoiceLine() { return freezeManualResumeVoiceLine; }

    // ---------------- Removal hook ----------------

    /**
     * Call this from your FreezeHandler when instance is removed.
     * This is where you do cooldown/cleanup/learning/removed effect, etc.
     */
    public void onRemoved(World world) {
        if (!world.isClientSide()) {
            if (action != null) {
                userPower.ifPresent(power -> {
                    if (power.hasPower()) {
                        action.onRemoved(power, ticksPassed);
                    }
                });
            }
            if (user != null && statusEffectInstance != null) {
                user.removeEffect(statusEffectInstance.getEffect());
            }
        }
    }

    // ---------------- Action interface ----------------

    /**
     * Minimal contract for freeze logic.
     * Put your cooldown/learning logic in onRemoved().
     */
    public interface IFreezeAction {
        boolean isUnlocked(IStandPower power);
        float getStaminaCostTicking(IStandPower power);
        void onRemoved(IStandPower power, int ticksPassed);
    }
}