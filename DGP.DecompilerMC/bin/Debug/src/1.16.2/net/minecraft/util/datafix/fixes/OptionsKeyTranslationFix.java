/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.stream.Collectors;
import net.minecraft.util.datafix.fixes.References;

public class OptionsKeyTranslationFix
extends DataFix {
    public OptionsKeyTranslationFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsKeyTranslationFix", this.getInputSchema().getType(References.OPTIONS), typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.getMapValues().map(map -> dynamic.createMap(map.entrySet().stream().map(entry -> {
            String string;
            if (((Dynamic)entry.getKey()).asString("").startsWith("key_") && !(string = ((Dynamic)entry.getValue()).asString("")).startsWith("key.mouse") && !string.startsWith("scancode.")) {
                return Pair.of(entry.getKey(), (Object)dynamic.createString("key.keyboard." + string.substring("key.".length())));
            }
            return Pair.of(entry.getKey(), entry.getValue());
        }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))).result().orElse(dynamic)));
    }
}

