package com.inza.exampleaddon.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

public class ServerUtils {
    public static DamageSource getCasterDamageSource(LivingEntity caster) {
        if (caster instanceof net.minecraft.entity.player.PlayerEntity) {
            return DamageSource.playerAttack((net.minecraft.entity.player.PlayerEntity) caster);
        }
        return DamageSource.mobAttack(caster);
    }
}
