package com.inza.exampleaddon.power.client;

import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.inza.exampleaddon.AddonMain;
import com.inza.exampleaddon.power.FearPower;
import com.inza.exampleaddon.power.FearPowerProvider;
import com.inza.exampleaddon.utils.MyUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = AddonMain.MOD_ID)
public class StandStatGUIHandler {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            // Use your Demon Stand registry name here
            ResourceLocation demonId = new ResourceLocation(AddonMain.MOD_ID, "demon_stand");

            StandStatsRenderer.overrideCosmeticStats(demonId, new StandStatsRenderer.ICosmeticStandStats() {

                @Override
                public double statConvertedValue(StandStatsRenderer.StandStat stat,
                                                 IStandPower power,
                                                 StandStats stats,
                                                 float dev) {
                    // 1) Get RotP's original converted value (this drives the radar graph & letter ranks)
                    double base = StandStatsRenderer.ICosmeticStandStats.DEFAULT
                            .statConvertedValue(stat, power, stats, dev);

                    // 2) Apply your dynamic multiplier
                    if (stat == StandStatsRenderer.StandStat.SPEED) {
//                        float mul = PowerServerManager.getSpeedMul(); // e.g. 1.0 ~ 1.5
                        FearPower fearPower = MyUtils.getFearCap(power.getUser()).orElse(null);
                        float mul = fearPower.getFear() / fearPower.getMaxFear();
                        System.out.println(fearPower == null ? "null" : fearPower.toString() + " mul " + mul);
                        return base * mul;
                    }

                    return base;
                }
            });
        });
    }
}
