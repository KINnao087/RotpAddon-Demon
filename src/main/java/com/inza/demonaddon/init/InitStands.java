package com.inza.demonaddon.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.inza.demonaddon.AddonMain;
import com.inza.demonaddon.action.domain.StandDomainAction;
import com.inza.demonaddon.action.domain.StandDomainClose;
import com.inza.demonaddon.action.freeze.FreezeAction;
import com.inza.demonaddon.client.render.model.DemonStandModel;
import com.inza.demonaddon.entity.DemonStandEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), AddonMain.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), AddonMain.MOD_ID);
    
 // ======================================== Example Stand ========================================
    
    
    // Create all the abilities here...

    public static final RegistryObject<StandEntityAction> DEMON_STAND_PUNCH = ACTIONS.register("demon_stand_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()
                    .punchSound(InitSounds.DEMON_STAND_PUNCH_LIGHT)));
    
    public static final RegistryObject<StandEntityAction> DEMON_STAND_BARRAGE = ACTIONS.register("demon_stand_barrage",
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()
                    .barrageHitSound(InitSounds.DEMON_STAND_PUNCH_BARRAGE)));

    public static final RegistryObject<StandEntityHeavyAttack> DEMON_STAND_FINISHER_PUNCH = ACTIONS.register("demon_stand_finisher_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder() // TODO finisher ability
                    .punchSound(InitSounds.DEMON_STAND_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityHeavyAttack> DEMON_STAND_HEAVY_PUNCH = ACTIONS.register("demon_stand_heavy_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder()
                    .shiftVariationOf(DEMON_STAND_PUNCH).shiftVariationOf(DEMON_STAND_BARRAGE)
                    .setFinisherVariation(DEMON_STAND_FINISHER_PUNCH)
                    .punchSound(InitSounds.DEMON_STAND_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)));


    public static final RegistryObject<StandDomainAction> DEMON_STAND_DOMAIN = ACTIONS.register("domain",
            () -> new StandDomainAction(new StandDomainAction.Builder()
                    .cooldown(360)
                    .staminaCostTick(75F)
                    .standPose(DemonStandModel.CHARGE_BURST)
                    .standSound(StandEntityAction.Phase.BUTTON_HOLD, ModSounds.STAND_SUMMON_DEFAULT)
                    .standSound(StandEntityAction.Phase.PERFORM, InitSounds.DEMON_STAND_START_DOMAIN).standPerformDuration(40)
                    .resolveLevelToUnlock(5)
                    .fearCostPerTick(0.1f)
                    .holdToFire(40, false)));
    public static final RegistryObject<StandEntityAction> DEMON_STAND_DOMAIN_CLOSE = ACTIONS.register("domain_close",
            () -> new StandDomainClose(new StandEntityAction.Builder()
                    .shiftVariationOf(DEMON_STAND_DOMAIN)));
    
    public static final RegistryObject<StandEntityAction> DEMON_STAND_BLOCK = ACTIONS.register("demon_stand_block",
            () -> new StandEntityBlock());

    public static final RegistryObject<StandEntityAction> DEMON_STAND_FREEZE = ACTIONS.register("demon_stand_freeze",
            () -> new FreezeAction(new FreezeAction.Builder()
                    .freezeParams(10, 5.5F, 180, 5)
                    .cooldown(250)
                    .standPose(StandPose.RANGED_ATTACK)
                    .standSound(StandEntityAction.Phase.PERFORM, InitSounds.DEMON_STAND_FREEZE).standPerformDuration(25)
                    .holdToFire(5, false)
                    .resolveLevelToUnlock(2)
                    .staminaCost(55F)
            ));


    // ...then create the Stand type instance. Moves, stats, entity sizes, and a few other things are determined here.
    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<DemonStandEntity>> STAND_DEMON_STAND = 
        new EntityStandRegistryObject<>("demon_stand", 
                STANDS, 
                () -> new EntityStandType.Builder<StandStats>()
                .color(0xAD0000)
                .storyPartName(ModStandsInit.PART_3_NAME)
                .leftClickHotbar(
                        DEMON_STAND_PUNCH.get(),
                        DEMON_STAND_BARRAGE.get()
                        )
                .rightClickHotbar(
                        DEMON_STAND_BLOCK.get(),
                        DEMON_STAND_DOMAIN.get(),
                        DEMON_STAND_FREEZE.get()
                        )
                .defaultStats(StandStats.class, new StandStats.Builder()
                        .tier(6)
                        .power(11, 13)
                        .speed(11, 13)
                        .range(8, 8)
                        .durability(11, 13)
                        .precision(11, 13)
                        .build())
                .addSummonShout(InitSounds.DEMON_STAND_SUMMON_VOICELINE)
                .addOst(InitSounds.DEMON_STAND_OST)
                .build(),
                
                InitEntities.ENTITIES,
                () -> new StandEntityType<DemonStandEntity>(DemonStandEntity::new, 0.7F, 2.1F)
                .summonSound(InitSounds.DEMON_STAND_SUMMON_SOUND)
                .unsummonSound(InitSounds.DEMON_STAND_UNSUMMON_SOUND))
        .withDefaultStandAttributes();

    // ======================================== ??? ========================================
    
    
    
}
