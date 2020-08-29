/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.OptionalDynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Codec;
import com.mojang.serialization.OptionalDynamic;
import java.util.List;
import net.minecraft.util.datafix.fixes.References;

public class EntityRedundantChanceTagsFix
extends DataFix {
    private static final Codec<List<Float>> FLOAT_LIST_CODEC = Codec.FLOAT.listOf();

    public EntityRedundantChanceTagsFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityRedundantChanceTagsFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            if (EntityRedundantChanceTagsFix.isZeroList(dynamic.get("HandDropChances"), 2)) {
                dynamic = dynamic.remove("HandDropChances");
            }
            if (EntityRedundantChanceTagsFix.isZeroList(dynamic.get("ArmorDropChances"), 4)) {
                dynamic = dynamic.remove("ArmorDropChances");
            }
            return dynamic;
        }));
    }

    private static boolean isZeroList(OptionalDynamic<?> optionalDynamic, int n) {
        return optionalDynamic.flatMap(FLOAT_LIST_CODEC::parse).map(list -> list.size() == n && list.stream().allMatch(f -> f.floatValue() == 0.0f)).result().orElse(false);
    }
}

