package com.inza.demonaddon.network;

import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.network.packet.S2CAddDomainPacket;
import com.inza.demonaddon.network.packet.S2CDemonVisionPacket;
import com.inza.demonaddon.network.packet.S2CFearSyncPacket;
import com.inza.demonaddon.network.packet.S2CForceCloseDomainPacket;

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

        CHANNEL.registerMessage(id++, S2CDemonVisionPacket.class,
                S2CDemonVisionPacket::encode,
                S2CDemonVisionPacket::decode,
                S2CDemonVisionPacket::handle);
    }

    public static void init() {
        if (CHANNEL != null) return;

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(AddonMain.MOD_ID, "network"),
                () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

        register();
    }
}
