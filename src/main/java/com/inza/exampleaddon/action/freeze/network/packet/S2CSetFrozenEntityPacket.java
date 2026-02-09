package com.inza.exampleaddon.action.freeze.network.packet;

import com.github.standobyte.jojo.capability.world.WorldUtilCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSetFrozenEntityPacket {
    private final int entityId;
    private final boolean frozen;

    public S2CSetFrozenEntityPacket(int entityId, boolean frozen) {
        this.entityId = entityId;
        this.frozen = frozen;
    }

    public static void encode(S2CSetFrozenEntityPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.frozen);
    }

    public static S2CSetFrozenEntityPacket decode(PacketBuffer buf) {
        return new S2CSetFrozenEntityPacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(S2CSetFrozenEntityPacket msg, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            World world = Minecraft.getInstance().level;
            if (world == null) return;

            Entity e = world.getEntity(msg.entityId);
            if (e == null) return;

            world.getCapability(WorldUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                // frozen=true -> canMove=false
                cap.getTimeStopHandler().updateEntityTimeStop(e, !msg.frozen, false);
            });
        });
        ctx.setPacketHandled(true);
    }
}
