/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class CatSitOnBlockGoal
extends MoveToBlockGoal {
    private final Cat cat;

    public CatSitOnBlockGoal(Cat cat, double d) {
        super(cat, d, 8);
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }

    @Override
    public void stop() {
        super.stop();
        this.cat.setInSittingPose(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.cat.setInSittingPose(this.isReachedTarget());
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        if (!levelReader.isEmptyBlock(blockPos.above())) {
            return false;
        }
        BlockState blockState = levelReader.getBlockState(blockPos);
        if (blockState.is(Blocks.CHEST)) {
            return ChestBlockEntity.getOpenCount(levelReader, blockPos) < 1;
        }
        if (blockState.is(Blocks.FURNACE) && blockState.getValue(FurnaceBlock.LIT).booleanValue()) {
            return true;
        }
        return blockState.is(BlockTags.BEDS, blockStateBase -> blockStateBase.getOptionalValue(BedBlock.PART).map(bedPart -> bedPart != BedPart.HEAD).orElse(true));
    }
}

