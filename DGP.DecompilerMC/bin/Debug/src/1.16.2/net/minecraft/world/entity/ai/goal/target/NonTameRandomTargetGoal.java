/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.minecraft.world.entity.ai.goal.target;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NonTameRandomTargetGoal<T extends LivingEntity>
extends NearestAttackableTargetGoal<T> {
    private final TamableAnimal tamableMob;

    public NonTameRandomTargetGoal(TamableAnimal tamableAnimal, Class<T> class_, boolean bl, @Nullable Predicate<LivingEntity> predicate) {
        super(tamableAnimal, class_, 10, bl, false, predicate);
        this.tamableMob = tamableAnimal;
    }

    @Override
    public boolean canUse() {
        return !this.tamableMob.isTame() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetConditions != null) {
            return this.targetConditions.test(this.mob, this.target);
        }
        return super.canContinueToUse();
    }
}

