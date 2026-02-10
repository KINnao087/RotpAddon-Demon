package com.inza.demonaddon.utils;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.power.FearPower;
import com.inza.demonaddon.power.FearPowerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

public class MyUtils {
    public static final String K_HAS_DEMON = "has_demon";

    private MyUtils() {}

    public static StandType<?> getStandType(LivingEntity player) {
        IStandPower sp = IStandPower.getStandPowerOptional(player).orElse(null);
        if (sp == null || !sp.hasPower()) return null;
        return sp.getType();
    }

    public static boolean hasMyStand(Entity player) {
        if ((player instanceof LivingEntity)) return false;
        StandType<?> type = getStandType((LivingEntity) player);
        if (type == null) return false;

        ResourceLocation id = type.getRegistryName();
        boolean ret = id != null && id.equals(new ResourceLocation(AddonMain.MOD_ID, "demon_stand"));
        return ret;
    }

    public static CompoundNBT getPersisted(PlayerEntity player) {
        CompoundNBT data = player.getPersistentData();
        if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG, 10)) {
            data.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
        }
        return data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
    }

    public static boolean getPersistedBoolean(PlayerEntity player, String key) {
        return getPersisted(player).getBoolean(key);
    }

    public static void setPersistedBoolean(PlayerEntity player, String key, boolean value) {
        getPersisted(player).putBoolean(key, value);
    }

    public static boolean hasDemon(PlayerEntity player) {
        // Persistent NBT written on server is not auto-synced to client.
        // Client must fall back to live capability check.
        if (player.level != null && player.level.isClientSide()) {
            return getPersistedBoolean(player, K_HAS_DEMON) || hasMyStand(player);
        }
        return getPersistedBoolean(player, K_HAS_DEMON);
    }

    /**
     * Convenience getter for the fear capability on a player.
     * Returns a LazyOptional so callers can chain resolve/ifPresent.
     */
    public static LazyOptional<FearPower> getFearCap(LivingEntity entity) {
        if (FearPowerProvider.FEAR_POWER_CAPABILITY == null) {
            System.out.println("null fear power capability");
            return LazyOptional.empty();
        }
        return entity.getCapability(FearPowerProvider.FEAR_POWER_CAPABILITY, null);
    }

    public static FearPower getFearPower(LivingEntity entity) {
        return getFearCap(entity).orElse(null);
    }
}
