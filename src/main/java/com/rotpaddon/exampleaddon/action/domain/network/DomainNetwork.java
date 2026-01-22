package com.rotpaddon.exampleaddon.action.domain.network;

import com.rotpaddon.exampleaddon.action.domain.network.packet.S2CAddDomainPacket;
import com.rotpaddon.exampleaddon.action.domain.network.packet.S2CForceCloseDomainPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public final class DomainNetwork {
    private DomainNetwork() {}

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
    }

    public static void init() {
        if (CHANNEL != null) return;

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("myrotpaddon", "domain"),
                () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

        CHANNEL.registerMessage(id++, S2CAddDomainPacket.class,
                S2CAddDomainPacket::encode,
                S2CAddDomainPacket::decode,
                S2CAddDomainPacket::handle);

        CHANNEL.registerMessage(id++, S2CForceCloseDomainPacket.class,
                S2CForceCloseDomainPacket::encode,
                S2CForceCloseDomainPacket::decode,
                S2CForceCloseDomainPacket::handle);
    }
}
