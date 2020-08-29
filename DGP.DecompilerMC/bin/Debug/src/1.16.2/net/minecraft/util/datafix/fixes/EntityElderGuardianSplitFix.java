/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import net.minecraft.util.datafix.fixes.SimpleEntityRenameFix;

public class EntityElderGuardianSplitFix
extends SimpleEntityRenameFix {
    public EntityElderGuardianSplitFix(Schema schema, boolean bl) {
        super("EntityElderGuardianSplitFix", schema, bl);
    }

    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(String string, Dynamic<?> dynamic) {
        return Pair.of((Object)(Objects.equals(string, "Guardian") && dynamic.get("Elder").asBoolean(false) ? "ElderGuardian" : string), dynamic);
    }
}

