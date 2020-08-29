/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.References;

public class OptionsLowerCaseLanguageFix
extends DataFix {
    public OptionsLowerCaseLanguageFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsLowerCaseLanguageFix", this.getInputSchema().getType(References.OPTIONS), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            Optional optional = dynamic.get("lang").asString().result();
            if (optional.isPresent()) {
                return dynamic.set("lang", dynamic.createString(((String)optional.get()).toLowerCase(Locale.ROOT)));
            }
            return dynamic;
        }));
    }
}

