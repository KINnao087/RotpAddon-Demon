package com.inza.demonaddon.init;

import com.inza.demonaddon.AddonMain;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.POTIONS, AddonMain.MOD_ID);

    public static int MAX_EFFECT_STACKS = 5;
    public static int MAX_EFFECT_DURATION = 200;
    public static final RegistryObject<Effect> HORRIFIED = EFFECTS.register("horrified",
            () -> new Effect(EffectType.NEUTRAL, 0x6B1128) {
            });
}
