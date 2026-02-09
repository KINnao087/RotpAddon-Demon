package com.inza.exampleaddon.action.freeze.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

public final class FreezeRenderer {
    private FreezeRenderer() {}

    public static int LAT = 48;
    public static int LON = 72;

    public static int TINT_R = 220;
    public static int TINT_G = 25;
    public static int TINT_B = 25;

    /**
     * 画一个“偏色反色球壳”（只靠 blend）：
     * - 覆盖到的屏幕区域：out = tint * (1 - dst)
     * - tint=白色 => 纯反色 out = (1 - dst)
     * - 只画单面：外部看外表面；内部看内表面，避免双面叠加抵消
     */
    public static void renderInvertSphere(MatrixStack ms,
                                          Vector3d cameraPos,
                                          Vector3d center,
                                          float radius) {
        if (radius <= 0.001F) return;

        boolean camInside = cameraPos.distanceTo(center) < (radius - 0.02);

        ms.pushPose();
        ms.translate(center.x - cameraPos.x, center.y - cameraPos.y, center.z - cameraPos.z);

        // ===== Render State =====
        RenderSystem.enableBlend();

        // 偏色反色的关键：out = src * (1 - dst)
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ZERO
        );

        RenderSystem.enableCull();
        GL11.glCullFace(camInside ? GL11.GL_FRONT : GL11.GL_BACK);

        if (isInside(cameraPos, center, radius)) RenderSystem.disableDepthTest();
        else RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        RenderSystem.disableTexture();

        Matrix4f mat = ms.last().pose();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuilder();
        bb.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);

        int r = clamp255(TINT_R);
        int g = clamp255(TINT_G);
        int b = clamp255(TINT_B);
        int a = 255; // 这里 alpha 基本没啥用（factor 不吃 SRC_ALPHA），留着无害

        for (int i = 0; i < LAT; i++) {
            double v0 = (double) i / (double) LAT;
            double v1 = (double) (i + 1) / (double) LAT;
            double phi0 = (v0 - 0.5) * Math.PI;
            double phi1 = (v1 - 0.5) * Math.PI;

            double y0 = Math.sin(phi0);
            double y1 = Math.sin(phi1);
            double c0 = Math.cos(phi0);
            double c1 = Math.cos(phi1);

            for (int j = 0; j < LON; j++) {
                double u0 = (double) j / (double) LON;
                double u1 = (double) (j + 1) / (double) LON;
                double theta0 = u0 * (Math.PI * 2.0);
                double theta1 = u1 * (Math.PI * 2.0);

                double x00 = c0 * Math.cos(theta0);
                double z00 = c0 * Math.sin(theta0);
                double x01 = c0 * Math.cos(theta1);
                double z01 = c0 * Math.sin(theta1);

                double x10 = c1 * Math.cos(theta0);
                double z10 = c1 * Math.sin(theta0);
                double x11 = c1 * Math.cos(theta1);
                double z11 = c1 * Math.sin(theta1);

                // quad -> 2 triangles
                v(bb, mat, (float) (x00 * radius), (float) (y0 * radius), (float) (z00 * radius), r, g, b, a);
                v(bb, mat, (float) (x10 * radius), (float) (y1 * radius), (float) (z10 * radius), r, g, b, a);
                v(bb, mat, (float) (x11 * radius), (float) (y1 * radius), (float) (z11 * radius), r, g, b, a);

                v(bb, mat, (float) (x00 * radius), (float) (y0 * radius), (float) (z00 * radius), r, g, b, a);
                v(bb, mat, (float) (x11 * radius), (float) (y1 * radius), (float) (z11 * radius), r, g, b, a);
                v(bb, mat, (float) (x01 * radius), (float) (y0 * radius), (float) (z01 * radius), r, g, b, a);
            }
        }

        tess.end();

        // ===== Restore State =====
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);

        GL11.glCullFace(GL11.GL_BACK);
        RenderSystem.disableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();

        ms.popPose();
    }

    private static void v(BufferBuilder bb, Matrix4f mat,
                          float x, float y, float z,
                          int r, int g, int b, int a) {
        bb.vertex(mat, x, y, z).color(r, g, b, a).endVertex();
    }

    private static int clamp255(int v) {
        return v < 0 ? 0 : Math.min(v, 255);
    }

    private static boolean isInside(Vector3d camPos, Vector3d rPos, float radius) {
        return camPos.distanceTo(rPos) <= radius;
    }
}
