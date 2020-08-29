/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class RandomBlockMatchTest
extends RuleTest {
    public static final Codec<RandomBlockMatchTest> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Registry.BLOCK.fieldOf("block").forGetter(randomBlockMatchTest -> randomBlockMatchTest.block), (App)Codec.FLOAT.fieldOf("probability").forGetter(randomBlockMatchTest -> Float.valueOf(randomBlockMatchTest.probability))).apply((Applicative)instance, RandomBlockMatchTest::new));
    private final Block block;
    private final float probability;

    public RandomBlockMatchTest(Block block, float f) {
        this.block = block;
        this.probability = f;
    }

    @Override
    public boolean test(BlockState blockState, Random random) {
        return blockState.is(this.block) && random.nextFloat() < this.probability;
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.RANDOM_BLOCK_TEST;
    }
}

