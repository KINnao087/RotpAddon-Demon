package com.inza.demonaddon.action.demonview.client;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.utils.ClientUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, value = Dist.CLIENT)
public class DemonViewRenderer {
    private static final float EXPAND_PER_TICK = 0.75F;
    private static final float RETRACT_PER_TICK = 0.75F;

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        if (!DemonViewClientState.enabled && !DemonViewClientState.retracting) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        updateRadius(mc.level.getGameTime());
        if (DemonViewClientState.currentRadius <= 0.0F) return;

        UUID casterUuid = DemonViewClientState.casterUuid;
        if (casterUuid == null) return;

        LivingEntity entity = event.getEntity();

        if (casterUuid.equals(entity.getUUID())) return;

        if (entity instanceof StandEntity) {
            StandEntity stand = (StandEntity) entity;
            LivingEntity user = stand.getUser();
            if (user != null && casterUuid.equals(user.getUUID())) {
                return;
            }
        }

        LivingEntity caster = ClientUtils.findLivingByUuid(mc.level, casterUuid);
        if (caster == null) return;

        float r = DemonViewClientState.currentRadius;

        double r2 = (double) r * (double) r;
        if (caster.distanceToSqr(entity) > r2) return;

        event.setCanceled(true);
        renderSolid(event, entity);
    }

    private static void updateRadius(long nowGameTime) {
        long last = DemonViewClientState.lastUpdateGameTime;
        if (last <= 0L) {
            DemonViewClientState.lastUpdateGameTime = nowGameTime;
            return;
        }

        long deltaTicks = nowGameTime - last;
        if (deltaTicks <= 0L) return;

        float delta = (float) deltaTicks;
        if (DemonViewClientState.enabled) {
            DemonViewClientState.currentRadius = Math.min(
                    DemonViewClientState.maxRadius,
                    DemonViewClientState.currentRadius + delta * EXPAND_PER_TICK
            );
        }
        else if (DemonViewClientState.retracting) {
            DemonViewClientState.currentRadius = Math.max(
                    0.0F,
                    DemonViewClientState.currentRadius - delta * RETRACT_PER_TICK
            );
            if (DemonViewClientState.currentRadius <= 0.0F) {
                DemonViewClientState.retracting = false;
                DemonViewClientState.casterUuid = null;
                DemonViewClientState.maxRadius = 0.0F;
            }
        }

        DemonViewClientState.lastUpdateGameTime = nowGameTime;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void renderSolid(RenderLivingEvent.Pre event, LivingEntity entity) {
        LivingRenderer renderer = event.getRenderer();
        EntityModel model = renderer.getModel();

        MatrixStack ms = event.getMatrixStack();
        IRenderTypeBuffer buffers = event.getBuffers();
        int packedLight = event.getLight();

        float pt = Minecraft.getInstance().getFrameTime();

        float limbSwing = entity.animationPosition - entity.animationSpeed * (1.0F - pt);
        float limbSwingAmount = MathHelper.lerp(pt, entity.animationSpeedOld, entity.animationSpeed);
        float ageInTicks = entity.tickCount + pt;

        float bodyYaw = MathHelper.rotLerp(pt, entity.yBodyRotO, entity.yBodyRot);
        float headYaw = MathHelper.rotLerp(pt, entity.yHeadRotO, entity.yHeadRot);
        float pitch = MathHelper.lerp(pt, entity.xRotO, entity.xRot);

        float netHeadYaw = headYaw - bodyYaw;

        ms.pushPose();

        Vector3d off = renderer.getRenderOffset(entity, pt);
        ms.translate(off.x, off.y, off.z);

        ms.mulPose(Vector3f.YP.rotationDegrees(180.0F - bodyYaw));

        ms.scale(-1.0F, -1.0F, 1.0F);
        ms.translate(0.0D, -1.501D, 0.0D);

        model.young = entity.isBaby();
        model.riding = entity.isPassenger();
        model.attackTime = 0.0F;
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, pitch);

        IVertexBuilder vb = buffers.getBuffer(DemonViewRenderTypes.DEMON_VISION);

        float r = 1.0F, g = 0.1F, b = 0.1F, a = 1F;
        model.renderToBuffer(ms, vb, 0x00F000F0, OverlayTexture.NO_OVERLAY, r, g, b, a);

        ms.popPose();
    }
}
