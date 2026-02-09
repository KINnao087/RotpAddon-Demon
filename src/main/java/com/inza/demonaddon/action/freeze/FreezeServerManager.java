package com.inza.demonaddon.action.freeze;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.WorldUtilCapProvider;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.action.domain.beans.DomainInstance;
import com.inza.demonaddon.action.freeze.network.FreezeNetwork;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public final class FreezeServerManager {

    // 每个 ServerWorld 一个 manager（WeakHashMap 避免世界卸载后内存泄露）
    private static final Map<ServerWorld, FreezeServerManager> MANAGERS = new WeakHashMap<>();

    private static FreezeServerManager of(ServerWorld w) {
        return MANAGERS.computeIfAbsent(w, FreezeServerManager::new);
    }

    private final ServerWorld world;
    private final Map<Integer, FreezeInstance> instances = new HashMap<>();
    private final Map<Integer, List<Entity>> entities = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(0);

    private FreezeServerManager(ServerWorld world) {
        this.world = world;
    }

    // ===================== 对外 API =====================

    /**
     * 从 FreezeAction 调用：
     * - DomainInstance 用于渲染（你本来就有）
     * - 这里会创建 FreezeInstance（逻辑）并开始冻结
     *
     * 注意：下面这套参数要与你 DomainInstance 构造一致
     */
    public static int addFreeze(World w,
                                DomainInstance areaForRender,
                                int ticks,
                                int expandTick,
                                float maxRadius,
                                int maxTicks,
                                int closeTick,
                                @Nullable LivingEntity user,
                                @Nullable FreezeInstance.IFreezeAction action) {
        if (!(w instanceof ServerWorld)) return -1;

        ServerWorld sw = (ServerWorld) w;
        return of(sw).addInternal(sw, areaForRender, ticks, expandTick, maxRadius, maxTicks, closeTick, user, action);
    }

    // 如果你不想传这么多参数，走这个：直接传 DomainInstance + user
    public static int addFreeze(World w,
                                DomainInstance area,
                                @Nullable LivingEntity user) {
        if (!(w instanceof ServerWorld)) return -1;
        return of((ServerWorld) w).addInternalSimple(area, user);
    }

    // ===================== 内部实现 =====================

    private int addInternalSimple(DomainInstance area, @Nullable LivingEntity user) {
        System.out.println("addInternalSimple: ");

        int id = nextId.getAndIncrement();

        // ✅ 你当前 FreezeInstance 没有 “FreezeInstance(int id, DomainInstance area, user, action)” 这种构造
        // 所以这里用你已有构造的“参数版”创建（下面按你 DomainInstance 的语义来填）
        //
        // !!! 这里必须与你 DomainInstance 构造保持一致 !!!
        // 我按你 action 里那套：expandTick, maxRadius, maxTicks, closeTick
        long startTick = world.getGameTime();

        // 你 FreezeInstance 构造里第三个参数 center，第4是 radiusBlocks，第5是 startTick
        // 第6/7/8 我按你那行 new DomainInstance(center, startTick, durationTicks, radiusBlocks, keepTicks, closeTicks, uuid)
        // 推断：durationTicks=expandTick, keepTicks=maxTicks, closeTicks=closeTick
        FreezeInstance inst = new FreezeInstance(
                world,
                area.keepTicks + area.durationTicks + area.closeTicks,             // 如果你 DomainInstance 里有 maxTicks 字段就用它；没有的话用你传入的
                area.center,                          // 你 FreezeInstance 里用了 area.center，说明 center 是 public
                area.maxRadius,                       // 同理：如果 public
                startTick,
                area.durationTicks,
                area.keepTicks,
                area.closeTicks,
                user,
                null
        );

        instances.put(id, inst);
        entities.put(id, new ArrayList<>());

        // 立刻生效
        float nowTickF = (float) world.getGameTime();
        for (Entity e : MCUtil.getAllEntities(world)) {
            if (shouldFreezeEntity(e, inst, nowTickF)) {
                applyFrozen(e, true);
                entities.get(id).add(e);
            }
        }

        return id;
    }

    private int addInternal(ServerWorld sw,
                            DomainInstance areaForRender,
                            int ticks,
                            int expandTick,
                            float maxRadius,
                            int maxTicks,
                            int closeTick,
                            @Nullable LivingEntity user,
                            @Nullable FreezeInstance.IFreezeAction action) {
        int id = nextId.getAndIncrement();
        long startTick = sw.getGameTime();

        // ✅ 创建逻辑实例：注意你 FreezeInstance 的构造参数命名很乱，但它内部就是 new DomainInstance(...)
        FreezeInstance inst = new FreezeInstance(
                sw,
                ticks,
                areaForRender.center,
                maxRadius,
                startTick,
                expandTick,   // 你 FreezeInstance 里传给 DomainInstance 的第三参（你命名成 durationTicks，但实际更像 expandTick）
                maxTicks,
                closeTick,
                user,
                action,
                id
        );

        instances.put(id, inst);
        entities.put(id, new ArrayList<>());
        // 立刻冻住当前范围内实体
        float nowTickF = (float) sw.getGameTime();
        for (Entity e : MCUtil.getAllEntities(sw)) {
            if (shouldFreezeEntity(e, inst, nowTickF)) {
                applyFrozen(e, true);
                entities.get(id).add(e);
            }
        }

        return id;
    }

    private void tick() {
        if (instances.isEmpty()) return;
//        System.out.println("none empty");

        // 1) tick & remove ended
        Iterator<Map.Entry<Integer, FreezeInstance>> it = instances.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, FreezeInstance> e = it.next();
            FreezeInstance inst = e.getValue();

            // 你 FreezeInstance.tick() 返回 true 表示结束
            if (inst.tick()) {
                it.remove();
                onRemovedFreeze(inst);
                inst.onRemoved(world);
            }
        }

        // 2) 刷新所有实体 stop 状态（稳但粗暴；性能不够再优化）
        float nowTickF = (float) world.getGameTime();

        for (Entity entity : MCUtil.getAllEntities(world)) {
            boolean freezeByAny = false;
            for (FreezeInstance inst : instances.values()) {
                if (shouldFreezeEntity(entity, inst, nowTickF)) {
                    freezeByAny = true;
                    break;
                }
            }

            // 合并 JJBA 时停
            boolean timeStop = TimeStopHandler.isTimeStopped(world, entity.blockPosition());
            boolean finalStop = freezeByAny || timeStop;
            applyFrozen(entity, finalStop);
//            entity.getCapability(EntityUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.updateEntityTimeStop(finalStop));
        }
    }

    private void onRemovedFreeze(FreezeInstance removed) {
        float nowTickF = (float) world.getGameTime();
        List<Entity> entitiesToRemove = entities.get(removed.getId());
        if (entitiesToRemove == null) return;

        for (Entity entity : entitiesToRemove) {
            // 只处理“这个 removed 覆盖范围内”的实体
            if (!removed.inRange3D(entity.getX(), entity.getY(), entity.getZ(), nowTickF)) {
                continue;
            }

            // 是否仍被其它 freeze 覆盖
            boolean stillFrozen = false;
            for (FreezeInstance inst : instances.values()) {
                if (inst.inRange3D(entity.getX(), entity.getY(), entity.getZ(), nowTickF)) {
                    stillFrozen = true;
                    break;
                }
            }

            // 与 RotP 时停合并：仍在时停就别解
            boolean timeStop = TimeStopHandler.isTimeStopped(world, entity.blockPosition());


            boolean finalStop = stillFrozen || timeStop;

            applyFrozen(entity, false);
        }
    }

    private boolean shouldFreezeEntity(Entity e, FreezeInstance inst, float nowTickF) {
        if (e == null || e.removed) return false;

        if (inst.user != null && inst.user.is(e)) return false;

        if (e instanceof StandEntity && ((StandEntity) e).getUser().equals(inst.user)) return false;

        if (e instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) e;
            if (p.isCreative() || p.isSpectator()) return false;
        }

        boolean ret = inst.inRange3D(e.getX(), e.getY(), e.getZ(), nowTickF);
        return ret;
    }

    private void setStopFlag(Entity e, boolean stop) {
        if (e == null || e.removed) return;

        world.getCapability(WorldUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            TimeStopHandler ts = cap.getTimeStopHandler();
            // stop=true  -> canMove=false
            // stop=false -> canMove=true
            ts.updateEntityTimeStop(e, !stop, false);
        });
    }

    private void applyFrozen(Entity e, boolean frozen) {
        if (e == null || e.removed) return;

        // 1) 服务端：停逻辑
        setStopFlag(e, frozen);
        // 2) 客户端：停动画
        syncFrozenToClients(e, frozen);
    }

    private void syncFrozenToClients(Entity e, boolean frozen) {
        // TRACKING_ENTITY_AND_SELF：追踪者 + 自己（如果实体是玩家自己也覆盖到）
        FreezeNetwork.CHANNEL.send(
                net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> e),
                new com.inza.demonaddon.action.freeze.network.packet.S2CSetFrozenEntityPacket(e.getId(), frozen)
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (!(event.world instanceof ServerWorld)) return;
        of((ServerWorld) event.world).tick();
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getWorld() instanceof ServerWorld)) return;

        ServerWorld sw = (ServerWorld) event.getWorld();
        FreezeServerManager mgr = of(sw);
        if (mgr.instances.isEmpty()) return;

        float nowTickF = (float) sw.getGameTime();
        Entity e = event.getEntity();

        for (FreezeInstance inst : mgr.instances.values()) {
            if (mgr.shouldFreezeEntity(e, inst, nowTickF)) {
                mgr.applyFrozen(e, true);
                break;
            }
        }
    }
}