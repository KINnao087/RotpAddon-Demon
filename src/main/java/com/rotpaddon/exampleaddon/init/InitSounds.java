package com.rotpaddon.exampleaddon.init;

import java.util.function.Supplier;

import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;
import com.rotpaddon.exampleaddon.AddonMain;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.github.standobyte.jojo.init.ModSounds;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            ForgeRegistries.SOUND_EVENTS, AddonMain.MOD_ID); // TODO sounds.json


    public static final RegistryObject<SoundEvent> DEMON_STAND_SUMMON_VOICELINE = SOUNDS.register("demon_stand_summon_voiceline", 
            () -> new SoundEvent(new ResourceLocation(AddonMain.MOD_ID, "demon_stand_summon_voiceline")));

    public static final Supplier<SoundEvent> DEMON_STAND_SUMMON_SOUND = ModSounds.STAND_SUMMON_DEFAULT;
    
    public static final Supplier<SoundEvent> DEMON_STAND_UNSUMMON_SOUND = ModSounds.STAND_UNSUMMON_DEFAULT;
    
    public static final Supplier<SoundEvent> DEMON_STAND_PUNCH_LIGHT = ModSounds.STAND_PUNCH_LIGHT;
    
    public static final Supplier<SoundEvent> DEMON_STAND_PUNCH_HEAVY = ModSounds.STAND_PUNCH_HEAVY;
    
    public static final Supplier<SoundEvent> DEMON_STAND_PUNCH_BARRAGE = ModSounds.STAND_PUNCH_LIGHT;
    
    public static final Supplier<SoundEvent> DEMON_STAND_START_DOMAIN = SOUNDS.register("domain_start",
                () -> new SoundEvent(new ResourceLocation(AddonMain.MOD_ID, "domain_start")));
    
    public static final OstSoundList DEMON_STAND_OST = new OstSoundList(
            new ResourceLocation(AddonMain.MOD_ID, "example_stand_ost"), SOUNDS);
}
