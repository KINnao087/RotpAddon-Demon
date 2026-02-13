package com.inza.demonaddon.power;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.AddonNetwork;
import com.inza.demonaddon.init.InitEffects;
import com.inza.demonaddon.power.network.packet.S2CFearSyncPacket;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class FearPowerHandler {
    private static final String K_HORRIFIED_LAST = AddonMain.MOD_ID + ":horrified_last";
    private static final float HORRIFIED_ABSORB_RADIUS = 100.0F;
    private static final float BASE_FEAR_GAIN_PER_SECOND_PER_ENTITY = 0.25F;
    private static final float MAX_FEAR_GAIN_PER_SECOND = 5.0F;
    private static final Map<String, Set<UUID>> HORRIFIED_BY_DIMENSION = new HashMap<>();

    @SubscribeEvent
    public static void onKillOtherEntity(final LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide()) return;
        removeHorrifiedEntity(event.getEntityLiving());

        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity)) return;

        if (attacker instanceof StandEntity) {
            LivingEntity owner = ((StandEntity) attacker).getUser();
            if (!(owner instanceof LivingEntity)) return;

            MyUtils.getFearCap(owner).ifPresent(fear -> {
                fear.addFear(5f);

                if (!MyUtils.hasMyStand(owner)) {return;}
                syncFearToTracking(owner, fear);
            });
            return;
        }

//        if (!(attacker instanceof PlayerEntity)) {return;}
        if (!MyUtils.hasMyStand(attacker)) return;
        MyUtils.getFearCap((LivingEntity) attacker).ifPresent(fear -> {
            fear.addFear(5f);
            syncFearToTracking((LivingEntity) attacker, fear);
        });
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.level.isClientSide()) return;
//        if (!(entity instanceof PlayerEntity)) return;
        if (!MyUtils.hasMyStand(entity)) return;

        MyUtils.getFearCap(entity).ifPresent(fear -> {
//            System.out.println("fearCostPertick = " + fear.getFearCostPerTick());
//            fear.subFear(fear.getFearCostPerTick());
            float gainPerSecond = getHorrifiedFearGainPerSecond(entity);
            if (gainPerSecond > 0.0F) {
                fear.addFear(gainPerSecond / 20.0F);
            }
        });
    }

    private static final float FEAR_TO_STAMINA_RATIO = 50F; // 1 Fear -> FEAR_TO_STAMINA_RATIO Stamina
    private static final float FEAR_TO_STAMINA_PER_SECOND = 25F; // max stamina gain per second from conversion
    private static final float FEAR_TO_STAMINA_PER_TICK = FEAR_TO_STAMINA_PER_SECOND / 20.0F;

    @SubscribeEvent
    public static void onFearConvertToStamina(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.level.isClientSide()) return;
        if (!MyUtils.hasMyStand(entity)) return;

        IStandPower standPower = IStandPower.getStandPowerOptional(entity).orElse(null);
        if (standPower == null || !standPower.hasPower()) return;
        if (!standPower.usesStamina()) return;

        float staminaNow = standPower.getStamina();
        float staminaMax = standPower.getMaxStamina();
        if (staminaNow >= staminaMax) return;
        if (FEAR_TO_STAMINA_RATIO <= 0.0F) return;

        MyUtils.getFearCap(entity).ifPresent(fear -> {
            float fearNow = fear.getFear();
            if (fearNow <= 0.0F) return;

            float missing = staminaMax - staminaNow;
            float staminaByFear = fearNow * FEAR_TO_STAMINA_RATIO;
            float staminaGain = Math.min(FEAR_TO_STAMINA_PER_TICK, Math.min(staminaByFear, missing));
            if (staminaGain <= 0.0F) return;

            float fearCost = staminaGain / FEAR_TO_STAMINA_RATIO;
            if (fear.subFear(fearCost)) {
                standPower.addStamina(staminaGain, true);
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerRenderGUITick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayerEntity)) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.player;

//         throttle: sync every 5 ticks
//        if ((player.tickCount % 5) != 0) return;

        MyUtils.getFearCap(player).ifPresent(fear -> {
            if (player.connection == null) return; // avoid login/respawn edge cases

            AddonNetwork.CHANNEL.sendTo(
                    new S2CFearSyncPacket(player.getId(), fear.getFear(), fear.getMaxFear()),
                    player.connection.connection,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }

    @SubscribeEvent
    public static void onHorrifiedEffectState(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.level.isClientSide()) return;

        boolean hasHorrified = entity.hasEffect(InitEffects.HORRIFIED.get());
        boolean last = entity.getPersistentData().getBoolean(K_HORRIFIED_LAST);
        if (hasHorrified == last) return;

        entity.getPersistentData().putBoolean(K_HORRIFIED_LAST, hasHorrified);
        String dimensionKey = entity.level.dimension().location().toString();
        Set<UUID> set = HORRIFIED_BY_DIMENSION.computeIfAbsent(dimensionKey, k -> new HashSet<>());
        if (hasHorrified) {
            set.add(entity.getUUID());
        } else {
            set.remove(entity.getUUID());
            if (set.isEmpty()) {
                HORRIFIED_BY_DIMENSION.remove(dimensionKey);
            }
        }
    }

    private static float getHorrifiedFearGainPerSecond(LivingEntity entity) {
        if (!(entity.level instanceof ServerWorld)) return 0.0F;
        ServerWorld world = (ServerWorld) entity.level;
        double radiusSqr = (double) HORRIFIED_ABSORB_RADIUS * (double) HORRIFIED_ABSORB_RADIUS;

        String dimensionKey = world.dimension().location().toString();
        Set<UUID> set = HORRIFIED_BY_DIMENSION.get(dimensionKey);
        if (set == null || set.isEmpty()) return 0.0F;

        float sum = 0.0F;
        Set<UUID> stale = null;
        for (UUID uuid : set) {
            Entity e = world.getEntity(uuid);
            if (!(e instanceof LivingEntity)) {
                if (stale == null) stale = new HashSet<>();
                stale.add(uuid);
                continue;
            }

            LivingEntity living = (LivingEntity) e;
            if (entity.distanceToSqr(living) > radiusSqr) {
                continue;
            }
            EffectInstance inst = living.getEffect(InitEffects.HORRIFIED.get());
            if (inst == null) {
                if (stale == null) stale = new HashSet<>();
                stale.add(uuid);
                continue;
            }

            int amp = Math.max(0, Math.min(inst.getAmplifier(), 4));
            int level = amp + 1;
            sum += BASE_FEAR_GAIN_PER_SECOND_PER_ENTITY * level;
        }

        if (stale != null) {
            set.removeAll(stale);
            if (set.isEmpty()) {
                HORRIFIED_BY_DIMENSION.remove(dimensionKey);
            }
        }

        return Math.min(sum, MAX_FEAR_GAIN_PER_SECOND);
    }

    private static void removeHorrifiedEntity(LivingEntity entity) {
        String dimensionKey = entity.level.dimension().location().toString();
        Set<UUID> set = HORRIFIED_BY_DIMENSION.get(dimensionKey);
        if (set == null) return;
        set.remove(entity.getUUID());
        if (set.isEmpty()) {
            HORRIFIED_BY_DIMENSION.remove(dimensionKey);
        }
    }

    private static void syncFearToTracking(LivingEntity entity, FearPower fear) {
        AddonNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                new S2CFearSyncPacket(entity.getId(), fear.getFear(), fear.getMaxFear())
        );
    }
}
