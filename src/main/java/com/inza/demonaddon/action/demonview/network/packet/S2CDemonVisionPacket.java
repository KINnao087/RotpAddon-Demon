package com.inza.demonaddon.action.demonview.network.packet;

import com.inza.demonaddon.action.demonview.client.DemonViewClientState;

public class S2CDemonVisionPacket {
    private final boolean enabled;
    private final java.util.UUID casterUuid;
    private final float radius;

    public S2CDemonVisionPacket(boolean enabled, java.util.UUID casterUuid, float radius) {
        this.enabled = enabled;
        this.casterUuid = casterUuid;
        this.radius = radius;
    }

    public static void encode(S2CDemonVisionPacket msg, net.minecraft.network.PacketBuffer buf) {
        buf.writeBoolean(msg.enabled);
        buf.writeUUID(msg.casterUuid);
        buf.writeFloat(msg.radius);
    }

    public static S2CDemonVisionPacket decode(net.minecraft.network.PacketBuffer buf) {
        return new S2CDemonVisionPacket(
                buf.readBoolean(),
                buf.readUUID(),
                buf.readFloat()
        );
    }

    public static void handle(S2CDemonVisionPacket msg, java.util.function.Supplier<net.minecraftforge.fml.network.NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.level == null || mc.player == null) return;
            if (!mc.player.getUUID().equals(msg.casterUuid)) return;

            if (!msg.enabled) {
                DemonViewClientState.enabled = false;
                DemonViewClientState.retracting = true;
                DemonViewClientState.lastUpdateGameTime = mc.level.getGameTime();
                return;
            }

            long now = mc.level.getGameTime();
            DemonViewClientState.enabled = true;
            DemonViewClientState.retracting = false;
            DemonViewClientState.casterUuid = msg.casterUuid;
            DemonViewClientState.maxRadius = Math.max(0.0F, msg.radius);
            DemonViewClientState.lastUpdateGameTime = now;
        });
        ctx.get().setPacketHandled(true);
    }
}
