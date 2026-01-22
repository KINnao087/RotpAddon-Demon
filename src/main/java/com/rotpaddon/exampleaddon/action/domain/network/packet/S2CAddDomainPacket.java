package com.rotpaddon.exampleaddon.action.domain.network.packet;

import java.util.UUID;

import com.rotpaddon.exampleaddon.action.domain.beans.DomainInstance;
import com.rotpaddon.exampleaddon.action.domain.client.DomainClientState;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CAddDomainPacket {
    public final double x, y, z;
    public final long startTick;
    public final int durationTicks;
    public final float maxRadius;
    public final int keepTicks;
    public final int closeTicks;
    public final UUID ownerUuid;

    public S2CAddDomainPacket(Vector3d center, long startTick, int durationTicks, float maxRadius, int keepTicks, int closeTicks, UUID ownerUuid) {
        this(center.x, center.y, center.z, startTick, durationTicks, maxRadius, keepTicks, closeTicks, ownerUuid);
    }
    public S2CAddDomainPacket(DomainInstance domainInstance) {
        this(   domainInstance.center,
                domainInstance.startTick,
                domainInstance.durationTicks,
                domainInstance.maxRadius,
                domainInstance.keepTicks,
                domainInstance.closeTicks,
                domainInstance.ownerUuid    );
    }

    public S2CAddDomainPacket(double x, double y, double z, long startTick, int durationTicks, float maxRadius, int keepTicks, int closeTicks, UUID ownerUuid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.startTick = startTick;
        this.durationTicks = durationTicks;
        this.maxRadius = maxRadius;
        this.keepTicks = keepTicks;
        this.closeTicks = closeTicks;
        this.ownerUuid = ownerUuid;
    }

    public static void encode(S2CAddDomainPacket msg, PacketBuffer buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeLong(msg.startTick);
        buf.writeInt(msg.durationTicks);
        buf.writeFloat(msg.maxRadius);
        buf.writeInt(msg.keepTicks);
        buf.writeInt(msg.closeTicks);
        buf.writeUUID(msg.ownerUuid);
    }

    public static S2CAddDomainPacket decode(PacketBuffer buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        long startTick = buf.readLong();
        int durationTicks = buf.readInt();
        float maxRadius = buf.readFloat();
        int keepTicks = buf.readInt();
        int closeTicks = buf.readInt();
        UUID owner = buf.readUUID();
        return new S2CAddDomainPacket(x, y, z, startTick, durationTicks, maxRadius, keepTicks, closeTicks, owner);
    }

    public static void handle(S2CAddDomainPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DomainInstance inst = new DomainInstance(new Vector3d(msg.x, msg.y, msg.z), msg.startTick,
                    msg.durationTicks, msg.maxRadius, msg.keepTicks, msg.closeTicks, msg.ownerUuid);
            DomainClientState.addOrReplace(inst);
        });
        ctx.get().setPacketHandled(true);
    }
}
