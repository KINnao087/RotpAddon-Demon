package com.inza.demonaddon.client.render.dynamictexture;

import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.client.render.model.DemonStandModel;
import com.inza.demonaddon.entity.DemonStandEntity;
import com.inza.demonaddon.power.FearPower;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DynamicTextureRenderer extends StandEntityRenderer<DemonStandEntity, DemonStandModel> {

    private static final ResourceLocation TEX_0 = new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand.png");
    private static final ResourceLocation TEX_1 = new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand1.png");
    private static final ResourceLocation TEX_2 = new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand2.png");

    public DynamicTextureRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DemonStandModel(), TEX_0, 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DemonStandEntity entity) {
        FearPower fearPower = MyUtils.getFearPower(entity.getUser());
        float fear = fearPower.getFear();
        float max = fearPower.getMaxFear();
        float t = max <= 0 ? 0f : (fear / max);

        if (t < 0.33f) return TEX_0;
        else if (t >= 0.33f && t <= 0.66f) return TEX_1;
        return TEX_2;
    }
}
