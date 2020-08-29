/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.OpticFinder
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 *  com.mojang.datafixers.util.Either
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.util.datafix.fixes.References;

public class EntityEquipmentToArmorAndHandFix
extends DataFix {
    public EntityEquipmentToArmorAndHandFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public TypeRewriteRule makeRule() {
        return this.cap(this.getInputSchema().getTypeRaw(References.ITEM_STACK));
    }

    private <IS> TypeRewriteRule cap(Type<IS> type) {
        Type type2 = DSL.and((Type)DSL.optional((Type)DSL.field((String)"Equipment", (Type)DSL.list(type))), (Type)DSL.remainderType());
        Type type3 = DSL.and((Type)DSL.optional((Type)DSL.field((String)"ArmorItems", (Type)DSL.list(type))), (Type)DSL.optional((Type)DSL.field((String)"HandItems", (Type)DSL.list(type))), (Type)DSL.remainderType());
        OpticFinder opticFinder = DSL.typeFinder((Type)type2);
        OpticFinder opticFinder2 = DSL.fieldFinder((String)"Equipment", (Type)DSL.list(type));
        return this.fixTypeEverywhereTyped("EntityEquipmentToArmorAndHandFix", this.getInputSchema().getType(References.ENTITY), this.getOutputSchema().getType(References.ENTITY), typed -> {
            Object object;
            Object object2;
            Object object3;
            Either either = Either.right((Object)DSL.unit());
            Either either2 = Either.right((Object)DSL.unit());
            Dynamic dynamic = (Dynamic)typed.getOrCreate(DSL.remainderFinder());
            Optional optional = typed.getOptional(opticFinder2);
            if (optional.isPresent()) {
                object3 = (List)optional.get();
                object2 = ((Pair)type.read(dynamic.emptyMap()).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created empty itemstack."))).getFirst();
                if (!object3.isEmpty()) {
                    either = Either.left((Object)Lists.newArrayList((Object[])new Object[]{object3.get(0), object2}));
                }
                if (object3.size() > 1) {
                    object = Lists.newArrayList((Object[])new Object[]{object2, object2, object2, object2});
                    for (int i = 1; i < Math.min(object3.size(), 5); ++i) {
                        object.set(i - 1, object3.get(i));
                    }
                    either2 = Either.left((Object)object);
                }
            }
            object3 = dynamic;
            object2 = dynamic.get("DropChances").asStreamOpt().result();
            if (((Optional)object2).isPresent()) {
                Dynamic dynamic2;
                object = Stream.concat((Stream)((Optional)object2).get(), Stream.generate(() -> EntityEquipmentToArmorAndHandFix.lambda$null$1((Dynamic)object3))).iterator();
                float f = ((Dynamic)object.next()).asFloat(0.0f);
                if (!dynamic.get("HandDropChances").result().isPresent()) {
                    dynamic2 = dynamic.createList(Stream.of(Float.valueOf(f), Float.valueOf(0.0f)).map(((Dynamic)dynamic)::createFloat));
                    dynamic = dynamic.set("HandDropChances", dynamic2);
                }
                if (!dynamic.get("ArmorDropChances").result().isPresent()) {
                    dynamic2 = dynamic.createList(Stream.of(Float.valueOf(((Dynamic)object.next()).asFloat(0.0f)), Float.valueOf(((Dynamic)object.next()).asFloat(0.0f)), Float.valueOf(((Dynamic)object.next()).asFloat(0.0f)), Float.valueOf(((Dynamic)object.next()).asFloat(0.0f))).map(((Dynamic)dynamic)::createFloat));
                    dynamic = dynamic.set("ArmorDropChances", dynamic2);
                }
                dynamic = dynamic.remove("DropChances");
            }
            return typed.set(opticFinder, type3, (Object)Pair.of((Object)either, (Object)Pair.of((Object)either2, (Object)dynamic)));
        });
    }

    private static /* synthetic */ Dynamic lambda$null$1(Dynamic dynamic) {
        return dynamic.createInt(0);
    }
}

