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

    public static final Supplier<SoundEvent> DEMON_STAND_SUMMON_SOUND = ModSounds.STAR_PLATINUM_SUMMON;
    
    public static final Supplier<SoundEvent> DEMON_STAND_UNSUMMON_SOUND = ModSounds.THE_WORLD_UNSUMMON;
    
    public static final Supplier<SoundEvent> DEMON_STAND_PUNCH_LIGHT = ModSounds.STAND_PUNCH_LIGHT;
    
    public static final Supplier<SoundEvent> DEMON_STAND_PUNCH_HEAVY = ModSounds.STAND_PUNCH_HEAVY;
    
    public static final Supplier<SoundEvent> DEMON_STAND_PUNCH_BARRAGE = ModSounds.STAND_PUNCH_LIGHT;

    public static final Supplier<SoundEvent> DEMON_STAND_FREEZE = SOUNDS.register("freeze",
            () -> new SoundEvent(new ResourceLocation(AddonMain.MOD_ID, "freeze")));
    
    public static final Supplier<SoundEvent> DEMON_STAND_START_DOMAIN = SOUNDS.register("domain_expand",
                () -> new SoundEvent(new ResourceLocation(AddonMain.MOD_ID, "domain_expand")));
    
    public static final OstSoundList DEMON_STAND_OST = new OstSoundList(
            new ResourceLocation(AddonMain.MOD_ID, "example_stand_ost"), SOUNDS);
}
