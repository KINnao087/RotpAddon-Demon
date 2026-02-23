package com.inza.demonaddon.power.network.packet;

import com.github.standobyte.jojo.client.ClientUtil;
import com.inza.demonaddon.utils.MyUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CFearSyncPacket {
    private final int entityId;
    private final float fear;
    private final float maxFear;

    public S2CFearSyncPacket(int entityId, float fear, float maxFear) {
        this.entityId = entityId;
        this.fear = fear;
        this.maxFear = maxFear;
    }

    public static void encode(S2CFearSyncPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeFloat(msg.fear);
        buf.writeFloat(msg.maxFear);
    }

    public static S2CFearSyncPacket decode(PacketBuffer buf) {
        return new S2CFearSyncPacket(buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(S2CFearSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ClientUtil.getClientPlayer();
            if (player == null) return;

            Entity target = player.level.getEntity(msg.entityId);
            if (!(target instanceof LivingEntity)) return;

            MyUtils.getFearCap((LivingEntity) target).ifPresent(cap -> {
                cap.setFear(msg.fear);
                cap.setMaxFear(msg.maxFear);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
