package com.inza.demonaddon.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.inza.demonaddon.power.FearPower;
import com.inza.demonaddon.utils.MyUtils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class DemonStandEntity extends StandEntity {
    private static final DataParameter<Integer> TEXTURE_STAGE =
            EntityDataManager.defineId(DemonStandEntity.class, DataSerializers.INT);

    public DemonStandEntity(StandEntityType<DemonStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TEXTURE_STAGE, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide() || (tickCount % 5) != 0) return;

        LivingEntity user = getUser();
        if (user == null) {
            setTextureStage(0);
            return;
        }

        FearPower fear = MyUtils.getFearPower(user);
        if (fear == null) {
            setTextureStage(0);
            return;
        }

        float max = fear.getMaxFear();
        float ratio = max <= 0.0F ? 0.0F : fear.getFear() / max;
        if (ratio < 0.33F) setTextureStage(0);
        else if (ratio <= 0.66F) setTextureStage(1);
        else setTextureStage(2);
    }

    public int getTextureStage() {
        return entityData.get(TEXTURE_STAGE);
    }

    public void setTextureStage(int stage) {
        int clamped = stage < 0 ? 0 : Math.min(stage, 2);
        entityData.set(TEXTURE_STAGE, clamped);
    }
}
