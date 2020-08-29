/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.level.block;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SmokerBlock
extends AbstractFurnaceBlock {
    protected SmokerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new SmokerBlockEntity();
    }

    @Override
    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof SmokerBlockEntity) {
            player.openMenu((MenuProvider)((Object)blockEntity));
            player.awardStat(Stats.INTERACT_WITH_SMOKER);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        if (!blockState.getValue(LIT).booleanValue()) {
            return;
        }
        double d = (double)blockPos.getX() + 0.5;
        double d2 = blockPos.getY();
        double d3 = (double)blockPos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(d, d2, d3, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        level.addParticle(ParticleTypes.SMOKE, d, d2 + 1.1, d3, 0.0, 0.0, 0.0);
    }
}

