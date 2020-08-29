/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.OpticFinder
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.References;

public class MapIdFix
extends DataFix {
    public MapIdFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    protected TypeRewriteRule makeRule() {
        Type type = this.getInputSchema().getType(References.SAVED_DATA);
        OpticFinder opticFinder = type.findField("data");
        return this.fixTypeEverywhereTyped("Map id fix", type, typed -> {
            Optional optional = typed.getOptionalTyped(opticFinder);
            if (optional.isPresent()) {
                return typed;
            }
            return typed.update(DSL.remainderFinder(), dynamic -> dynamic.createMap((Map)ImmutableMap.of((Object)dynamic.createString("data"), (Object)dynamic)));
        });
    }
}

