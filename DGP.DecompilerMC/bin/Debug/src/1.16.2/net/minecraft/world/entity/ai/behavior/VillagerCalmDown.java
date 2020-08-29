/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.VillagerPanicTrigger;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;

public class VillagerCalmDown
extends Behavior<Villager> {
    public VillagerCalmDown() {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel serverLevel, Villager villager, long l) {
        boolean bl;
        boolean bl2 = bl = VillagerPanicTrigger.isHurt(villager) || VillagerPanicTrigger.hasHostile(villager) || VillagerCalmDown.isCloseToEntityThatHurtMe(villager);
        if (!bl) {
            villager.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
            villager.getBrain().eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            villager.getBrain().updateActivityFromSchedule(serverLevel.getDayTime(), serverLevel.getGameTime());
        }
    }

    private static boolean isCloseToEntityThatHurtMe(Villager villager) {
        return villager.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).filter(livingEntity -> livingEntity.distanceToSqr(villager) <= 36.0).isPresent();
    }
}

