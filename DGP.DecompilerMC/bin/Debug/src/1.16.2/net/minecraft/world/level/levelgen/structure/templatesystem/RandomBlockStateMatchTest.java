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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class RandomBlockStateMatchTest
extends RuleTest {
    public static final Codec<RandomBlockStateMatchTest> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("block_state").forGetter(randomBlockStateMatchTest -> randomBlockStateMatchTest.blockState), (App)Codec.FLOAT.fieldOf("probability").forGetter(randomBlockStateMatchTest -> Float.valueOf(randomBlockStateMatchTest.probability))).apply((Applicative)instance, RandomBlockStateMatchTest::new));
    private final BlockState blockState;
    private final float probability;

    public RandomBlockStateMatchTest(BlockState blockState, float f) {
        this.blockState = blockState;
        this.probability = f;
    }

    @Override
    public boolean test(BlockState blockState, Random random) {
        return blockState == this.blockState && random.nextFloat() < this.probability;
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.RANDOM_BLOCKSTATE_TEST;
    }
}

