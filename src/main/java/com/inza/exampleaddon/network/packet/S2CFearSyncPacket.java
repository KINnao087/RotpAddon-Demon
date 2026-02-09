package com.inza.exampleaddon.network.packet;

import com.inza.exampleaddon.utils.MyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CFearSyncPacket {
    private final float fear;
    private final float maxFear;

    public S2CFearSyncPacket(float fear, float maxFear) {
        this.fear = fear;
        this.maxFear = maxFear;
    }

    public static void encode(S2CFearSyncPacket msg, PacketBuffer buf) {
        buf.writeFloat(msg.fear);
        buf.writeFloat(msg.maxFear);
    }

    public static S2CFearSyncPacket decode(PacketBuffer buf) {
        return new S2CFearSyncPacket(buf.readFloat(), buf.readFloat());
    }

    public static void handle(S2CFearSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null) return;

            MyUtils.getFearCap(player).ifPresent(cap -> {
                cap.setFear(msg.fear);
                cap.setMaxFear(msg.maxFear);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
