/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.entity.ai.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;

public class GoalUtils {
    public static boolean hasGroundPathNavigation(Mob mob) {
        return mob.getNavigation() instanceof GroundPathNavigation;
    }
}

