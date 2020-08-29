/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL$TypeReference
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class WriteAndReadFix
extends DataFix {
    private final String name;
    private final DSL.TypeReference type;

    public WriteAndReadFix(Schema schema, String string, DSL.TypeReference typeReference) {
        super(schema, true);
        this.name = string;
        this.type = typeReference;
    }

    protected TypeRewriteRule makeRule() {
        return this.writeAndRead(this.name, this.getInputSchema().getType(this.type), this.getOutputSchema().getType(this.type));
    }
}

