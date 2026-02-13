package com.inza.demonaddon.client.render.renderer;

import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.client.render.model.DemonStandModel;
import com.inza.demonaddon.entity.DemonStandEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DemonStandRenderer extends StandEntityRenderer<DemonStandEntity, DemonStandModel> {

    private static final ResourceLocation TEX_0 = new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand.png");
    private static final ResourceLocation TEX_1 = new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand1.png");
    private static final ResourceLocation TEX_2 = new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand2.png");

    public DemonStandRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DemonStandModel(), TEX_0, 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DemonStandEntity entity) {
        int stage = entity.getTextureStage();
        if (stage <= 0) return TEX_0;
        if (stage == 1) return TEX_1;
        return TEX_2;
    }
}
