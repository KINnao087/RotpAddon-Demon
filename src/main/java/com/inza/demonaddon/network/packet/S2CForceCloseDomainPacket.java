package com.inza.demonaddon.network.packet;

import java.util.UUID;
import java.util.function.Supplier;

import com.inza.demonaddon.action.domain.client.DomainClientState;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2CForceCloseDomainPacket {
    public final UUID ownerUuid;
    public final long nowTick;

    public S2CForceCloseDomainPacket(UUID ownerUuid, long nowTick) {
        this.ownerUuid = ownerUuid;
        this.nowTick = nowTick;
    }

    public static void encode(S2CForceCloseDomainPacket msg, PacketBuffer buf) {
        buf.writeUUID(msg.ownerUuid);
        buf.writeLong(msg.nowTick);
    }

    public static S2CForceCloseDomainPacket decode(PacketBuffer buf) {
        UUID owner = buf.readUUID();
        long nowTick = buf.readLong();
        return new S2CForceCloseDomainPacket(owner, nowTick);
    }

    public static void handle(S2CForceCloseDomainPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DomainClientState.forceCloseByOwner(msg.ownerUuid, msg.nowTick));
        ctx.get().setPacketHandled(true);
    }
}
