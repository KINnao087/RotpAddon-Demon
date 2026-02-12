package com.inza.demonaddon.action.demonview.client;

import com.inza.demonaddon.AddonMain;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;

public class DemonViewRenderTypes {
    private static final ResourceLocation WHITE_TEX =
            new ResourceLocation(AddonMain.MOD_ID, "textures/misc/white.png");

    public static final RenderType DEMON_VISION =
            new com.github.standobyte.jojo.client.render.rendertype.ModifiedRenderType(
                    RenderType.entityTranslucent(WHITE_TEX),
                    () -> {
                        // Setup state: through walls
                        RenderSystem.enableBlend();
                        RenderSystem.disableDepthTest();
                        RenderSystem.depthMask(false);

                        RenderHelper.turnOff();
                    },
                    () -> {
                        // Restore state
                        RenderHelper.turnBackOn();
                        RenderSystem.depthMask(true);
                        RenderSystem.enableDepthTest();
                        RenderSystem.disableBlend();
                    },
                    "demon_vision"
            );

    private DemonViewRenderTypes() {}
}
