package com.inza.exampleaddon.network;

import com.inza.exampleaddon.AddonMain;
import com.inza.exampleaddon.network.packet.S2CAddDomainPacket;
import com.inza.exampleaddon.network.packet.S2CFearSyncPacket;
import com.inza.exampleaddon.network.packet.S2CForceCloseDomainPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public final class AddonNetwork {
    private AddonNetwork() {}

    private static final String PROTOCOL = "1";

    public static SimpleChannel CHANNEL;

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, S2CAddDomainPacket.class,
                S2CAddDomainPacket::encode,
                S2CAddDomainPacket::decode,
                S2CAddDomainPacket::handle);

        CHANNEL.registerMessage(id++, S2CForceCloseDomainPacket.class,
                S2CForceCloseDomainPacket::encode,
                S2CForceCloseDomainPacket::decode,
                S2CForceCloseDomainPacket::handle);

        CHANNEL.registerMessage(id++, S2CFearSyncPacket.class,
                S2CFearSyncPacket::encode,
                S2CFearSyncPacket::decode,
                S2CFearSyncPacket::handle);
    }

    public static void init() {
        if (CHANNEL != null) return;

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(AddonMain.MOD_ID, "network"),
                () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

        register();
    }
}
