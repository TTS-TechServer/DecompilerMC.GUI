/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.serialization.Dynamic
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import java.util.Set;
import net.minecraft.util.datafix.fixes.References;

public class EntityHealthFix
extends DataFix {
    private static final Set<String> ENTITIES = Sets.newHashSet((Object[])new String[]{"ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie"});

    public EntityHealthFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public Dynamic<?> fixTag(Dynamic<?> dynamic) {
        float f;
        Optional optional = dynamic.get("HealF").asNumber().result();
        Optional optional2 = dynamic.get("Health").asNumber().result();
        if (optional.isPresent()) {
            f = ((Number)optional.get()).floatValue();
            dynamic = dynamic.remove("HealF");
        } else if (optional2.isPresent()) {
            f = ((Number)optional2.get()).floatValue();
        } else {
            return dynamic;
        }
        return dynamic.set("Health", dynamic.createFloat(f));
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityHealthFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), this::fixTag));
    }
}

