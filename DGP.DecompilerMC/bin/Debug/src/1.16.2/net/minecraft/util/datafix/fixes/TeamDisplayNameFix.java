/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.DataFixUtils
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.datafix.fixes.References;

public class TeamDisplayNameFix
extends DataFix {
    public TeamDisplayNameFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    protected TypeRewriteRule makeRule() {
        Type type = DSL.named((String)References.TEAM.typeName(), (Type)DSL.remainderType());
        if (!Objects.equals((Object)type, (Object)this.getInputSchema().getType(References.TEAM))) {
            throw new IllegalStateException("Team type is not what was expected.");
        }
        return this.fixTypeEverywhere("TeamDisplayNameFix", type, dynamicOps -> pair -> pair.mapSecond(dynamic -> dynamic.update("DisplayName", dynamic2 -> (Dynamic)DataFixUtils.orElse((Optional)dynamic2.asString().map(string -> Component.Serializer.toJson(new TextComponent((String)string))).map(((Dynamic)dynamic)::createString).result(), (Object)dynamic2))));
    }
}

