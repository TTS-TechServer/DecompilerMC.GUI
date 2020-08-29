/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonsterRoomFeature
extends Feature<NoneFeatureConfiguration> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final EntityType<?>[] MOBS = new EntityType[]{EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER};
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public MonsterRoomFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, NoneFeatureConfiguration noneFeatureConfiguration) {
        Object object;
        BlockPos blockPos2;
        int n;
        int n2;
        int n3;
        int n4 = 3;
        int n5 = random.nextInt(2) + 2;
        int n6 = -n5 - 1;
        int n7 = n5 + 1;
        int n8 = -1;
        int n9 = 4;
        int n10 = random.nextInt(2) + 2;
        int n11 = -n10 - 1;
        int n12 = n10 + 1;
        int n13 = 0;
        for (n3 = n6; n3 <= n7; ++n3) {
            for (n2 = -1; n2 <= 4; ++n2) {
                for (n = n11; n <= n12; ++n) {
                    blockPos2 = blockPos.offset(n3, n2, n);
                    object = worldGenLevel.getBlockState(blockPos2).getMaterial();
                    boolean bl = ((Material)object).isSolid();
                    if (n2 == -1 && !bl) {
                        return false;
                    }
                    if (n2 == 4 && !bl) {
                        return false;
                    }
                    if (n3 != n6 && n3 != n7 && n != n11 && n != n12 || n2 != 0 || !worldGenLevel.isEmptyBlock(blockPos2) || !worldGenLevel.isEmptyBlock(blockPos2.above())) continue;
                    ++n13;
                }
            }
        }
        if (n13 < 1 || n13 > 5) {
            return false;
        }
        for (n3 = n6; n3 <= n7; ++n3) {
            for (n2 = 3; n2 >= -1; --n2) {
                for (n = n11; n <= n12; ++n) {
                    blockPos2 = blockPos.offset(n3, n2, n);
                    object = worldGenLevel.getBlockState(blockPos2);
                    if (n3 == n6 || n2 == -1 || n == n11 || n3 == n7 || n2 == 4 || n == n12) {
                        if (blockPos2.getY() >= 0 && !worldGenLevel.getBlockState(blockPos2.below()).getMaterial().isSolid()) {
                            worldGenLevel.setBlock(blockPos2, AIR, 2);
                            continue;
                        }
                        if (!((BlockBehaviour.BlockStateBase)object).getMaterial().isSolid() || ((BlockBehaviour.BlockStateBase)object).is(Blocks.CHEST)) continue;
                        if (n2 == -1 && random.nextInt(4) != 0) {
                            worldGenLevel.setBlock(blockPos2, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 2);
                            continue;
                        }
                        worldGenLevel.setBlock(blockPos2, Blocks.COBBLESTONE.defaultBlockState(), 2);
                        continue;
                    }
                    if (((BlockBehaviour.BlockStateBase)object).is(Blocks.CHEST) || ((BlockBehaviour.BlockStateBase)object).is(Blocks.SPAWNER)) continue;
                    worldGenLevel.setBlock(blockPos2, AIR, 2);
                }
            }
        }
        block6: for (n3 = 0; n3 < 2; ++n3) {
            for (n2 = 0; n2 < 3; ++n2) {
                int n14;
                int n15;
                n = blockPos.getX() + random.nextInt(n5 * 2 + 1) - n5;
                BlockPos blockPos3 = new BlockPos(n, n15 = blockPos.getY(), n14 = blockPos.getZ() + random.nextInt(n10 * 2 + 1) - n10);
                if (!worldGenLevel.isEmptyBlock(blockPos3)) continue;
                int n16 = 0;
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (!worldGenLevel.getBlockState(blockPos3.relative(direction)).getMaterial().isSolid()) continue;
                    ++n16;
                }
                if (n16 != 1) continue;
                worldGenLevel.setBlock(blockPos3, StructurePiece.reorient(worldGenLevel, blockPos3, Blocks.CHEST.defaultBlockState()), 2);
                RandomizableContainerBlockEntity.setLootTable(worldGenLevel, random, blockPos3, BuiltInLootTables.SIMPLE_DUNGEON);
                continue block6;
            }
        }
        worldGenLevel.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 2);
        BlockEntity blockEntity = worldGenLevel.getBlockEntity(blockPos);
        if (blockEntity instanceof SpawnerBlockEntity) {
            ((SpawnerBlockEntity)blockEntity).getSpawner().setEntityId(this.randomEntityId(random));
        } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", (Object)blockPos.getX(), (Object)blockPos.getY(), (Object)blockPos.getZ());
        }
        return true;
    }

    private EntityType<?> randomEntityId(Random random) {
        return Util.getRandom(MOBS, random);
    }
}

