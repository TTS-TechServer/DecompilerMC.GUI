/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;

public class IglooMetadataRemovalFix
extends DataFix {
    public IglooMetadataRemovalFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    protected TypeRewriteRule makeRule() {
        Type type = this.getInputSchema().getType(References.STRUCTURE_FEATURE);
        Type type2 = this.getOutputSchema().getType(References.STRUCTURE_FEATURE);
        return this.writeFixAndRead("IglooMetadataRemovalFix", type, type2, IglooMetadataRemovalFix::fixTag);
    }

    private static <T> Dynamic<T> fixTag(Dynamic<T> dynamic) {
        boolean bl = dynamic.get("Children").asStreamOpt().map(stream -> stream.allMatch(IglooMetadataRemovalFix::isIglooPiece)).result().orElse(false);
        if (bl) {
            return dynamic.set("id", dynamic.createString("Igloo")).remove("Children");
        }
        return dynamic.update("Children", IglooMetadataRemovalFix::removeIglooPieces);
    }

    private static <T> Dynamic<T> removeIglooPieces(Dynamic<T> dynamic) {
        return dynamic.asStreamOpt().map(stream -> stream.filter(dynamic -> !IglooMetadataRemovalFix.isIglooPiece(dynamic))).map(dynamic::createList).result().orElse(dynamic);
    }

    private static boolean isIglooPiece(Dynamic<?> dynamic) {
        return dynamic.get("id").asString("").equals("Iglu");
    }
}

