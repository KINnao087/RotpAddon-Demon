package com.rotpaddon.exampleaddon.utils;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;

public class ClientUtils {

    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static PlayerEntity getPlayerByUUID(UUID playerUuid) {
        Minecraft mc = getMinecraft();
        if (mc.level == null) return null;
        return mc.level.getPlayerByUUID(playerUuid);
    }

    public static long getNowTicks() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return 0L;
        return mc.level.getGameTime();
    }

    public static void playSound(RegistryObject<SoundEvent> soundType, float soundVolume, double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        mc.level.playLocalSound(
                x, y, z,
                soundType.get(),
                SoundCategory.AMBIENT,
                soundVolume,
                1.0f,
                false
        );
    }

    public static ClientPlayerEntity getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    public static IRenderTypeBuffer getMultiBufferSource() {
        Minecraft mc = Minecraft.getInstance();
        // 1.16.5: IRenderTypeBuffer.Impl
        IRenderTypeBuffer.Impl buffer = mc.renderBuffers().bufferSource();
        return buffer;
    }

    public static float getParticalTick() {
        return Minecraft.getInstance().getFrameTime();
    }

    public static void playSound(RegistryObject<SoundEvent> soundType, float soundVolume) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        ClientPlayerEntity player = getLocalPlayer();
        if (player == null) return;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        mc.level.playLocalSound(
                x, y, z,
                soundType.get(),
                SoundCategory.AMBIENT,
                soundVolume,
                2.0f,
                false
        );
    }

    public static void playSound(SoundEvent sound, float soundVolume) {
        playSound(sound, SoundCategory.AMBIENT, soundVolume, 1.0f);
    }

    public static void playSound(SoundEvent sound, SoundCategory source, float volume, float pitch) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        mc.execute(() -> {
            if (mc.level == null) return;
            ClientPlayerEntity player = mc.player;
            if (player == null) return;
            System.out.println("Playing sound: " + sound);

            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            mc.level.playLocalSound(x, y, z, sound, source, volume, pitch, false);
        });
    }

    public static int destFromKeys(Minecraft mc) {
        int mask = 0;

        if (mc.options.keyUp.isDown()) mask |= 1;     // W
        if (mc.options.keyRight.isDown()) mask |= 2;  // D
        if (mc.options.keyDown.isDown()) mask |= 4;   // S
        if (mc.options.keyLeft.isDown()) mask |= 8;   // A

        if ((mask & 1) != 0 && (mask & 4) != 0) mask &= ~(1 | 4);
        if ((mask & 2) != 0 && (mask & 8) != 0) mask &= ~(2 | 8);

        switch (mask) {
            case 0:      return 4; //
            case 1:      return 0; // W
            case 3:      return 1; // W+D
            case 2:      return 2; // D
            case 6:      return 3; // S+D
            case 4:      return 4; // S
            case 12:     return 5; // S+A
            case 8:      return 6; // A
            case 9:      return 7; // W+A
            default:     return 0;
        }
    }
}