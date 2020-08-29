/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;
import net.minecraft.util.datafix.fixes.References;

public class AdvancementsRenameFix
extends DataFix {
    private final String name;
    private final Function<String, String> renamer;

    public AdvancementsRenameFix(Schema schema, boolean bl, String string, Function<String, String> function) {
        super(schema, bl);
        this.name = string;
        this.renamer = function;
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.name, this.getInputSchema().getType(References.ADVANCEMENTS), typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.updateMapValues(pair -> {
            String string = ((Dynamic)pair.getFirst()).asString("");
            return pair.mapFirst(dynamic2 -> dynamic.createString(this.renamer.apply(string)));
        })));
    }
}

