package com.rotpaddon.exampleaddon.action.freeze.network;

import com.rotpaddon.exampleaddon.AddonMain;
import com.rotpaddon.exampleaddon.action.freeze.network.packet.S2CAddFreezeDomainPacket;
import com.rotpaddon.exampleaddon.action.freeze.network.packet.S2CSetFrozenEntityPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class FreezeNetwork {
    private FreezeNetwork() {}

    private static final String PROTOCOL = "1";
    public static SimpleChannel CHANNEL;
    private static int id = 0;

    public static void init() {
        if (CHANNEL != null) return;

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(AddonMain.MOD_ID, "freeze"),
                () -> PROTOCOL,
                PROTOCOL::equals,
                PROTOCOL::equals
        );

        // S2C 包：建议明确方向 PLAY_TO_CLIENT（1.16.5 这样更稳）
        CHANNEL.registerMessage(
                id++,
                S2CAddFreezeDomainPacket.class,
                S2CAddFreezeDomainPacket::encode,
                S2CAddFreezeDomainPacket::decode,
                S2CAddFreezeDomainPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        CHANNEL.registerMessage(id++,
                S2CSetFrozenEntityPacket.class,
                S2CSetFrozenEntityPacket::encode,
                S2CSetFrozenEntityPacket::decode,
                S2CSetFrozenEntityPacket::handle
        );

    }
}
