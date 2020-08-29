/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class EntityWolfColorFix
extends NamedEntityFix {
    public EntityWolfColorFix(Schema schema, boolean bl) {
        super(schema, bl, "EntityWolfColorFix", References.ENTITY, "minecraft:wolf");
    }

    public Dynamic<?> fixTag(Dynamic<?> dynamic2) {
        return dynamic2.update("CollarColor", dynamic -> dynamic.createByte((byte)(15 - dynamic.asInt(0))));
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
}

