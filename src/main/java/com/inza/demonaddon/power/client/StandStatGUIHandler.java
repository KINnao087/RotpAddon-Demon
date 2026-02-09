package com.inza.demonaddon.power.client;

import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.power.FearScaling;
import com.inza.demonaddon.power.Stat;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;

/**
 * Dynamically adjusts stand stats panel (radar graph + letter ranks) based on Fear.
 *
 * NOTE: Cosmetic/UI only, does NOT change real combat values.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = AddonMain.MOD_ID)
public class StandStatGUIHandler {

    private static final Logger LOGGER = LogManager.getLogger(AddonMain.MOD_ID);

    private static final ResourceLocation DEMON_ID =
            new ResourceLocation(AddonMain.MOD_ID, "demon_stand");

    // UI smoothing only (0..1). Set 0 for instant response.
    private static final double SMOOTHING = 0.90;

    private static final Map<StandStatsRenderer.StandStat, Double> SMOOTHED =
            new EnumMap<>(StandStatsRenderer.StandStat.class);

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            StandStatsRenderer.overrideCosmeticStats(DEMON_ID, new StandStatsRenderer.ICosmeticStandStats() {

                @Override
                public double statConvertedValue(StandStatsRenderer.StandStat stat,
                                                 IStandPower power,
                                                 StandStats stats,
                                                 float dev) {

                    // RotP original converted value (drives radar graph + rank)
                    double base = StandStatsRenderer.ICosmeticStandStats.DEFAULT
                            .statConvertedValue(stat, power, stats, dev);

                    // Resolve user safely
                    LivingEntity user = power != null ? power.getUser() : null;
                    if (user == null) user = Minecraft.getInstance().player;
                    if (user == null) return base;

                    // Map RotP UI stat -> our external Stat
                    Stat mapped = mapToExternalStat(stat);
                    if (mapped == null) {
                        return base; // unchanged UI stat (RANGE / DEV_POTENTIAL)
                    }

                    // 100% use external FearScaling
                    double mult = FearScaling.computeMultiplier(user, mapped);
                    if (!Double.isFinite(mult)) return base;

                    // Cosmetic smoothing only
                    mult = smooth(stat, mult);

                    double out = base * mult;
                    return Double.isFinite(out) ? out : base;
                }
            });

            LOGGER.info("Registered cosmetic stand stats override for {}", DEMON_ID);
        });
    }

    /**
     * Map RotP stats panel dimensions to your external Stat enum.
     */
    private static Stat mapToExternalStat(StandStatsRenderer.StandStat stat) {
        switch (stat) {
            case STRENGTH:
                return Stat.DAMAGE;
            case SPEED:
                return Stat.SPEED;
            case DURABILITY:
                return Stat.DURABILITY;
            case PRECISION:
                return Stat.PRECISION;
            default:
                return null;
        }
    }

    private static double smooth(StandStatsRenderer.StandStat stat, double target) {
        double prev = SMOOTHED.getOrDefault(stat, 1.0);
        double next = prev * SMOOTHING + target * (1.0 - SMOOTHING);
        if (!Double.isFinite(next)) next = target;
        SMOOTHED.put(stat, next);
        return next;
    }
}