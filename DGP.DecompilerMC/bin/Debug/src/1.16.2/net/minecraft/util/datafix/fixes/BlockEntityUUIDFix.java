/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.AbstractUUIDFix;
import net.minecraft.util.datafix.fixes.References;

public class BlockEntityUUIDFix
extends AbstractUUIDFix {
    public BlockEntityUUIDFix(Schema schema) {
        super(schema, References.BLOCK_ENTITY);
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("BlockEntityUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> {
            typed = this.updateNamedChoice((Typed<?>)typed, "minecraft:conduit", this::updateConduit);
            typed = this.updateNamedChoice((Typed<?>)typed, "minecraft:skull", this::updateSkull);
            return typed;
        });
    }

    private Dynamic<?> updateSkull(Dynamic<?> dynamic3) {
        return dynamic3.get("Owner").get().map(dynamic -> BlockEntityUUIDFix.replaceUUIDString(dynamic, "Id", "Id").orElse((Dynamic<?>)dynamic)).map(dynamic2 -> dynamic3.remove("Owner").set("SkullOwner", dynamic2)).result().orElse(dynamic3);
    }

    private Dynamic<?> updateConduit(Dynamic<?> dynamic) {
        return BlockEntityUUIDFix.replaceUUIDMLTag(dynamic, "target_uuid", "Target").orElse(dynamic);
    }
}

