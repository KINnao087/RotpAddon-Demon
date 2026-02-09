package com.inza.demonaddon.action.freeze;

import com.github.standobyte.jojo.action.config.ActionConfigField;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.inza.demonaddon.action.domain.beans.DomainInstance;
import com.inza.demonaddon.action.freeze.network.FreezeNetwork;
import com.inza.demonaddon.action.freeze.network.packet.S2CAddFreezeDomainPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class FreezeAction extends StandEntityAction {

    @ActionConfigField private final int expandTicks;
    @ActionConfigField private final int closeTicks;
    @ActionConfigField private final int durationTicks;
    @ActionConfigField private final float radiusBlocks;

    @ActionConfigField private final float staminaCost;        // 启动瞬时消耗（跟 DomainAction 一样直接返回）
    @ActionConfigField private final float staminaCostPerTick; // 每tick消耗（按 max stamina 缩放）

    public FreezeAction(FreezeAction.Builder builder) {
        super(builder);
        this.expandTicks = builder.expandTicks;
        this.closeTicks = builder.closeTicks;
        this.durationTicks = builder.durationTicks;
        this.radiusBlocks = builder.radiusBlocks;

        this.staminaCost = builder.staminaCost;
        this.staminaCostPerTick = builder.staminaCostPerTick;
    }

    // ===== stamina =====

    @Override
    public float getStaminaCost(IStandPower power) {
        return this.staminaCost;
    }

    /**
     * RotP/JoJo 里常用的是 ticking cost（每tick扣）
     * 这里按 DomainAction 的风格：perTick * maxStamina
     */
    @Override
    public float getStaminaCostTicking(IStandPower power) {
        return this.staminaCostPerTick * power.getMaxStamina();
    }

    // ===== action =====

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        // 只在服务端生成实例 + 广播给客户端
        if (world.isClientSide()) {
            return;
        }

        LivingEntity user = userPower.getUser();
        if (user == null) {
            return;
        }

        RayTraceResult hit = JojoModUtil.rayTrace(user, 100, e -> e != standEntity);
        if (hit == null || hit.getType() == RayTraceResult.Type.MISS) {
            return;
        }

        Vector3d center = hit.getLocation();
        if (center == null) {
            return;
        }

        long nowTick = world.getGameTime();

        DomainInstance renderInst = new DomainInstance(
                center,
                nowTick,
                this.expandTicks,
                this.radiusBlocks,
                this.durationTicks,
                this.closeTicks,
                user.getUUID()
        );

        // ✅ 服务端真正冻结逻辑
        FreezeServerManager.addFreeze(world, renderInst, user);

        // ✅ 广播给追踪玩家 + 自己（渲染用）
        FreezeNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> user),
                new S2CAddFreezeDomainPacket(renderInst)
        );

        // 冷却（按持续时间走，跟 DomainAction 一样）
        userPower.setCooldownTimer(this, this.durationTicks);
    }

    // ===== builder =====

    public static class Builder extends StandEntityAction.AbstractBuilder<Builder> {
        private int expandTicks = 5;
        private int closeTicks = 0;
        private int durationTicks = 280;
        private float radiusBlocks = 5.0f;

        private float staminaCost = 0.02f;
        private float staminaCostPerTick = 0.01f;

        public Builder freezeParams(int expandTicks, float radiusBlocks, int durationTicks, int closeTicks) {
            this.expandTicks = Math.max(0, expandTicks);
            this.radiusBlocks = Math.max(0.0f, radiusBlocks);
            this.durationTicks = Math.max(0, durationTicks);
            this.closeTicks = Math.max(0, closeTicks);
            return getThis();
        }

        public Builder expandTicks(int ticks) {
            this.expandTicks = Math.max(0, ticks);
            return getThis();
        }

        public Builder closeTicks(int ticks) {
            this.closeTicks = Math.max(0, ticks);
            return getThis();
        }

        public Builder durationTicks(int ticks) {
            this.durationTicks = Math.max(0, ticks);
            return getThis();
        }

        public Builder radiusBlocks(float r) {
            this.radiusBlocks = Math.max(0.0f, r);
            return getThis();
        }

        public Builder staminaCost(float staminaCost) {
            this.staminaCost = staminaCost;
            return getThis();
        }

        public Builder staminaCostPerTick(float staminaCostPerTick) {
            this.staminaCostPerTick = staminaCostPerTick;
            return getThis();
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
