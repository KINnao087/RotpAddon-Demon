package com.inza.exampleaddon.action.domain.client;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

public final class DomainRenderer {
    private DomainRenderer() {}

    public static int LAT = 48;
    public static int LON = 72;

    public static void renderSolidSphere(MatrixStack ms,
                                         Vector3d cameraPos,
                                         Vector3d center,
                                         float radius,
                                         int r, int g, int b, int a) {
        if (radius <= 0.001F) return;

        ms.pushPose();
        ms.translate(center.x - cameraPos.x, center.y - cameraPos.y, center.z - cameraPos.z);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableTexture();

        Matrix4f mat = ms.last().pose();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuilder();
        bb.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);

        // lat: [-pi/2, pi/2], lon: [0, 2pi)
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

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        ms.popPose();
    }

    private static void v(BufferBuilder bb, Matrix4f mat,
                          float x, float y, float z,
                          int r, int g, int b, int a) {
        bb.vertex(mat, x, y, z).color(r, g, b, a).endVertex();
    }
}
