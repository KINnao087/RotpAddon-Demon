package com.inza.demonaddon.client.render.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class DemonRenderTypes {
    private static final ResourceLocation WHITE_TEX =
            new ResourceLocation("minecraft", "textures/misc/white.png");

    public static final RenderType DEMON_SILHOUETTE =
            new com.github.standobyte.jojo.client.render.rendertype.ModifiedRenderType(
                    RenderType.entityTranslucent(WHITE_TEX),
                    () -> {
                        // Disable depth test so it renders through walls
                        RenderSystem.disableDepthTest();
                        RenderSystem.depthMask(false); // Don't write to depth buffer
                    },
                    () -> {
                        // Restore state
                        RenderSystem.depthMask(true);
                        RenderSystem.enableDepthTest();
                    },
                    "demon_silhouette"
            );

    private DemonRenderTypes() {}
}
