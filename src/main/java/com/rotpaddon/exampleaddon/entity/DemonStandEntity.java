package com.rotpaddon.exampleaddon.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class DemonStandEntity extends StandEntity {

    public DemonStandEntity(StandEntityType<DemonStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
}
