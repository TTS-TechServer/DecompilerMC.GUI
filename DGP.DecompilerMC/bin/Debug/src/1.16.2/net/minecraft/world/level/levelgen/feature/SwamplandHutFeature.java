/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.SwamplandHutPiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class SwamplandHutFeature
extends StructureFeature<NoneFeatureConfiguration> {
    private static final List<MobSpawnSettings.SpawnerData> SWAMPHUT_ENEMIES = ImmutableList.of((Object)new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1));
    private static final List<MobSpawnSettings.SpawnerData> SWAMPHUT_ANIMALS = ImmutableList.of((Object)new MobSpawnSettings.SpawnerData(EntityType.CAT, 1, 1, 1));

    public SwamplandHutFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return FeatureStart::new;
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
        return SWAMPHUT_ENEMIES;
    }

    @Override
    public List<MobSpawnSettings.SpawnerData> getSpecialAnimals() {
        return SWAMPHUT_ANIMALS;
    }

    public static class FeatureStart
    extends StructureStart<NoneFeatureConfiguration> {
        public FeatureStart(StructureFeature<NoneFeatureConfiguration> structureFeature, int n, int n2, BoundingBox boundingBox, int n3, long l) {
            super(structureFeature, n, n2, boundingBox, n3, l);
        }

        @Override
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, int n, int n2, Biome biome, NoneFeatureConfiguration noneFeatureConfiguration) {
            SwamplandHutPiece swamplandHutPiece = new SwamplandHutPiece(this.random, n * 16, n2 * 16);
            this.pieces.add(swamplandHutPiece);
            this.calculateBoundingBox();
        }
    }
}

