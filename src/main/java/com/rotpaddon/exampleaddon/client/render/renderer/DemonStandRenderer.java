package com.rotpaddon.exampleaddon.client.render.renderer;

import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.rotpaddon.exampleaddon.AddonMain;
import com.rotpaddon.exampleaddon.client.render.model.DemonStandModel;
import com.rotpaddon.exampleaddon.entity.DemonStandEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DemonStandRenderer extends StandEntityRenderer<DemonStandEntity, StandEntityModel<DemonStandEntity>> {
    
    public DemonStandRenderer(EntityRendererManager renderManager) {
        super(renderManager, 
                StandModelRegistry.registerModel(new ResourceLocation(AddonMain.MOD_ID, "demon_stand"), DemonStandModel::new),
                new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/demon_stand.png"), 0);
    }
}
