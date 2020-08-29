/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class BlockMatchTest
extends RuleTest {
    public static final Codec<BlockMatchTest> CODEC = Registry.BLOCK.fieldOf("block").xmap(BlockMatchTest::new, blockMatchTest -> blockMatchTest.block).codec();
    private final Block block;

    public BlockMatchTest(Block block) {
        this.block = block;
    }

    @Override
    public boolean test(BlockState blockState, Random random) {
        return blockState.is(this.block);
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.BLOCK_TEST;
    }
}

