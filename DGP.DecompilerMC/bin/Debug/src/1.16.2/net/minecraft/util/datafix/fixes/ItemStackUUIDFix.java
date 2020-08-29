/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.OpticFinder
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.AbstractUUIDFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemStackUUIDFix
extends AbstractUUIDFix {
    public ItemStackUUIDFix(Schema schema) {
        super(schema, References.ITEM_STACK);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder opticFinder = DSL.fieldFinder((String)"id", (Type)DSL.named((String)References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        return this.fixTypeEverywhereTyped("ItemStackUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> {
            OpticFinder opticFinder2 = typed.getType().findField("tag");
            return typed.updateTyped(opticFinder2, typed2 -> typed2.update(DSL.remainderFinder(), dynamic -> {
                dynamic = this.updateAttributeModifiers((Dynamic<?>)dynamic);
                if (typed.getOptional(opticFinder).map(pair -> "minecraft:player_head".equals(pair.getSecond())).orElse(false).booleanValue()) {
                    dynamic = this.updateSkullOwner((Dynamic<?>)dynamic);
                }
                return dynamic;
            }));
        });
    }

    private Dynamic<?> updateAttributeModifiers(Dynamic<?> dynamic) {
        return dynamic.update("AttributeModifiers", dynamic3 -> dynamic.createList(dynamic3.asStream().map(dynamic -> ItemStackUUIDFix.replaceUUIDLeastMost(dynamic, "UUID", "UUID").orElse((Dynamic<?>)dynamic))));
    }

    private Dynamic<?> updateSkullOwner(Dynamic<?> dynamic2) {
        return dynamic2.update("SkullOwner", dynamic -> ItemStackUUIDFix.replaceUUIDString(dynamic, "Id", "Id").orElse((Dynamic<?>)dynamic));
    }
}

