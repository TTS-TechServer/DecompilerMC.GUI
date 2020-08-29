/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.OpticFinder
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class EntityCustomNameToComponentFix
extends DataFix {
    public EntityCustomNameToComponentFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder opticFinder = DSL.fieldFinder((String)"id", NamespacedSchema.namespacedString());
        return this.fixTypeEverywhereTyped("EntityCustomNameToComponentFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            Optional optional = typed.getOptional(opticFinder);
            if (optional.isPresent() && Objects.equals(optional.get(), "minecraft:commandblock_minecart")) {
                return dynamic;
            }
            return EntityCustomNameToComponentFix.fixTagCustomName(dynamic);
        }));
    }

    public static Dynamic<?> fixTagCustomName(Dynamic<?> dynamic) {
        String string = dynamic.get("CustomName").asString("");
        if (string.isEmpty()) {
            return dynamic.remove("CustomName");
        }
        return dynamic.set("CustomName", dynamic.createString(Component.Serializer.toJson(new TextComponent(string))));
    }
}

