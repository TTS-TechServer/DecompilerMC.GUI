/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownExperienceBottle
extends ThrowableItemProjectile {
    public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> entityType, Level level) {
        super((EntityType<? extends ThrowableItemProjectile>)entityType, level);
    }

    public ThrownExperienceBottle(Level level, LivingEntity livingEntity) {
        super((EntityType<? extends ThrowableItemProjectile>)EntityType.EXPERIENCE_BOTTLE, livingEntity, level);
    }

    public ThrownExperienceBottle(Level level, double d, double d2, double d3) {
        super((EntityType<? extends ThrowableItemProjectile>)EntityType.EXPERIENCE_BOTTLE, d, d2, d3, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.EXPERIENCE_BOTTLE;
    }

    @Override
    protected float getGravity() {
        return 0.07f;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level.isClientSide) {
            int n;
            this.level.levelEvent(2002, this.blockPosition(), PotionUtils.getColor(Potions.WATER));
            for (int i = 3 + this.level.random.nextInt(5) + this.level.random.nextInt(5); i > 0; i -= n) {
                n = ExperienceOrb.getExperienceValue(i);
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), n));
            }
            this.remove();
        }
    }
}

