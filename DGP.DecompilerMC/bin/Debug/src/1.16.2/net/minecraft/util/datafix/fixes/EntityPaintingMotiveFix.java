/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFixUtils
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class EntityPaintingMotiveFix
extends NamedEntityFix {
    private static final Map<String, String> MAP = (Map)DataFixUtils.make((Object)Maps.newHashMap(), hashMap -> {
        hashMap.put("donkeykong", "donkey_kong");
        hashMap.put("burningskull", "burning_skull");
        hashMap.put("skullandroses", "skull_and_roses");
    });

    public EntityPaintingMotiveFix(Schema schema, boolean bl) {
        super(schema, bl, "EntityPaintingMotiveFix", References.ENTITY, "minecraft:painting");
    }

    public Dynamic<?> fixTag(Dynamic<?> dynamic) {
        Optional optional = dynamic.get("Motive").asString().result();
        if (optional.isPresent()) {
            String string = ((String)optional.get()).toLowerCase(Locale.ROOT);
            return dynamic.set("Motive", dynamic.createString(new ResourceLocation(MAP.getOrDefault(string, string)).toString()));
        }
        return dynamic;
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
}

