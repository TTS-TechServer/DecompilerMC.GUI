/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class OminousBannerBlockEntityRenameFix
extends NamedEntityFix {
    public OminousBannerBlockEntityRenameFix(Schema schema, boolean bl) {
        super(schema, bl, "OminousBannerBlockEntityRenameFix", References.BLOCK_ENTITY, "minecraft:banner");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }

    private Dynamic<?> fixTag(Dynamic<?> dynamic) {
        Optional optional = dynamic.get("CustomName").asString().result();
        if (optional.isPresent()) {
            String string = (String)optional.get();
            string = string.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"");
            return dynamic.set("CustomName", dynamic.createString(string));
        }
        return dynamic;
    }
}

