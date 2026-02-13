package com.inza.demonaddon.commands;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandManifestation;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.AddonNetwork;
import com.inza.demonaddon.power.network.packet.S2CFearSyncPacket;
import com.inza.demonaddon.utils.MyUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FearCommands {
    private FearCommands() {}

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("fearpower")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("add")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                .executes(FearCommands::executeFearAdd))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                .executes(FearCommands::executeFearSet))))
                        .then(Commands.literal("get")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(FearCommands::executeFearGet)))
        );

        dispatcher.register(
                Commands.literal("getstandstats")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("playername", EntityArgument.player())
                                .executes(FearCommands::executeGetStandStats))
        );
    }

    private static int executeFearAdd(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(ctx, "targets");
        float value = FloatArgumentType.getFloat(ctx, "value");
        int success = 0;

        for (ServerPlayerEntity target : targets) {
            boolean ok = MyUtils.getFearCap(target).map(cap -> {
                cap.addFear(value);
                syncFear(target, cap.getFear(), cap.getMaxFear());
                return true;
            }).orElse(false);
            if (ok) success++;
        }

        if (success == 0) {
            ctx.getSource().sendFailure(new StringTextComponent("No target has FearPower capability."));
            return 0;
        }

        ctx.getSource().sendSuccess(
                new StringTextComponent("Added fear " + value + " to " + success + " player(s)."),
                true
        );
        return success;
    }

    private static int executeFearSet(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(ctx, "targets");
        float value = FloatArgumentType.getFloat(ctx, "value");
        int success = 0;

        for (ServerPlayerEntity target : targets) {
            boolean ok = MyUtils.getFearCap(target).map(cap -> {
                float clamped = Math.max(0.0F, Math.min(value, cap.getMaxFear()));
                cap.setFear(clamped);
                syncFear(target, cap.getFear(), cap.getMaxFear());
                return true;
            }).orElse(false);
            if (ok) success++;
        }

        if (success == 0) {
            ctx.getSource().sendFailure(new StringTextComponent("No target has FearPower capability."));
            return 0;
        }

        ctx.getSource().sendSuccess(
                new StringTextComponent("Set fear to " + value + " for " + success + " player(s)."),
                true
        );
        return success;
    }

    private static int executeFearGet(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(ctx, "targets");
        int success = 0;

        for (ServerPlayerEntity target : targets) {
            boolean ok = MyUtils.getFearCap(target).map(cap -> {
                ctx.getSource().sendSuccess(
                        new StringTextComponent(target.getName().getString() + ": "
                                + cap.getFear() + "/" + cap.getMaxFear()),
                        false
                );
                return true;
            }).orElse(false);
            if (ok) success++;
        }

        if (success == 0) {
            ctx.getSource().sendFailure(new StringTextComponent("No target has FearPower capability."));
            return 0;
        }
        return success;
    }

    private static void syncFear(ServerPlayerEntity target, float fear, float maxFear) {
        AddonNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target),
                new S2CFearSyncPacket(target.getId(), fear, maxFear)
        );
    }

    private static int executeGetStandStats(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "playername");
        IStandPower standPower = IStandPower.getStandPowerOptional(target).orElse(null);
        if (standPower == null || !standPower.hasPower()) {
            ctx.getSource().sendFailure(new StringTextComponent("Target has no stand power."));
            return 0;
        }

        IStandManifestation manifestation = standPower.getStandManifestation();
        if (!(manifestation instanceof StandEntity)) {
            ctx.getSource().sendFailure(new StringTextComponent("StandEntity is not available. Summon stand first."));
            return 0;
        }

        StandEntity stand = (StandEntity) manifestation;

        ctx.getSource().sendSuccess(new StringTextComponent("AttackDamage: " + fmt(stand.getAttackDamage())), false);
        ctx.getSource().sendSuccess(new StringTextComponent("AttackSpeed: " + fmt(stand.getAttackSpeed())), false);
        ctx.getSource().sendSuccess(new StringTextComponent("Durability: " + fmt(stand.getDurability())), false);
        ctx.getSource().sendSuccess(new StringTextComponent("Precision: " + fmt(stand.getPrecision())), false);
        ctx.getSource().sendSuccess(new StringTextComponent("MaxRange: " + fmt(stand.getMaxRange())), false);
        return 1;
    }

    private static String fmt(double v) {
        return String.format("%.2f", v);
    }
}
