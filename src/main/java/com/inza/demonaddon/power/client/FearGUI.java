package com.inza.demonaddon.power.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FearGUI {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null) return;
        if (!MyUtils.hasMyStand(mc.player)) return;

//        mc.player.getCapability(FearPowerProvider.FEAR_POWER_CAPABILITY).ifPresent(fear -> {
//            FearPower fear = MyUtils.getFearPower(mc.player);
//        System.out.println(fear.getFear() + " / " + fear.getMaxFear());
        MyUtils.getFearCap(mc.player).ifPresent(fear -> {
            MatrixStack ms = event.getMatrixStack();
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();

            int barWidth = 140;
            int barHeight = 10;
            int x = sw / 2 - barWidth / 2;
            int y = sh - 52;

            float ratio = fear.getMaxFear() <= 0 ? 0 : (fear.getFear() / fear.getMaxFear());
            ratio = Math.max(0f, Math.min(1f, ratio));
            int fill = (int) (ratio * barWidth);

            AbstractGui.fill(ms, x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0xAA000000);
            AbstractGui.fill(ms, x, y, x + barWidth, y + barHeight, 0xFF2B1E24);
            AbstractGui.fill(ms, x, y, x + fill, y + barHeight, 0xFFE1395F);

            String label = String.format("Fear %.0f / %.0f", fear.getFear(), fear.getMaxFear());
            mc.font.drawShadow(ms, label, x, y - 10, 0xFFF8DDE4);
        });
    }
}
