/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import java.util.Objects;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class RenameBiomesFix
extends DataFix {
    private final String name;
    private final Map<String, String> biomes;

    public RenameBiomesFix(Schema schema, boolean bl, String string, Map<String, String> map) {
        super(schema, bl);
        this.biomes = map;
        this.name = string;
    }

    protected TypeRewriteRule makeRule() {
        Type type = DSL.named((String)References.BIOME.typeName(), NamespacedSchema.namespacedString());
        if (!Objects.equals((Object)type, (Object)this.getInputSchema().getType(References.BIOME))) {
            throw new IllegalStateException("Biome type is not what was expected.");
        }
        return this.fixTypeEverywhere(this.name, type, dynamicOps -> pair -> pair.mapSecond(string -> this.biomes.getOrDefault(string, (String)string)));
    }
}

