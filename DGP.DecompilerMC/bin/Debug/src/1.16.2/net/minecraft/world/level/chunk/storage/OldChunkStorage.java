/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.level.chunk.storage;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.OldDataLayer;

public class OldChunkStorage {
    public static OldLevelChunk load(CompoundTag compoundTag) {
        int n = compoundTag.getInt("xPos");
        int n2 = compoundTag.getInt("zPos");
        OldLevelChunk oldLevelChunk = new OldLevelChunk(n, n2);
        oldLevelChunk.blocks = compoundTag.getByteArray("Blocks");
        oldLevelChunk.data = new OldDataLayer(compoundTag.getByteArray("Data"), 7);
        oldLevelChunk.skyLight = new OldDataLayer(compoundTag.getByteArray("SkyLight"), 7);
        oldLevelChunk.blockLight = new OldDataLayer(compoundTag.getByteArray("BlockLight"), 7);
        oldLevelChunk.heightmap = compoundTag.getByteArray("HeightMap");
        oldLevelChunk.terrainPopulated = compoundTag.getBoolean("TerrainPopulated");
        oldLevelChunk.entities = compoundTag.getList("Entities", 10);
        oldLevelChunk.blockEntities = compoundTag.getList("TileEntities", 10);
        oldLevelChunk.blockTicks = compoundTag.getList("TileTicks", 10);
        try {
            oldLevelChunk.lastUpdated = compoundTag.getLong("LastUpdate");
        }
        catch (ClassCastException classCastException) {
            oldLevelChunk.lastUpdated = compoundTag.getInt("LastUpdate");
        }
        return oldLevelChunk;
    }

    public static void convertToAnvilFormat(RegistryAccess.RegistryHolder registryHolder, OldLevelChunk oldLevelChunk, CompoundTag compoundTag, BiomeSource biomeSource) {
        compoundTag.putInt("xPos", oldLevelChunk.x);
        compoundTag.putInt("zPos", oldLevelChunk.z);
        compoundTag.putLong("LastUpdate", oldLevelChunk.lastUpdated);
        int[] arrn = new int[oldLevelChunk.heightmap.length];
        for (int i = 0; i < oldLevelChunk.heightmap.length; ++i) {
            arrn[i] = oldLevelChunk.heightmap[i];
        }
        compoundTag.putIntArray("HeightMap", arrn);
        compoundTag.putBoolean("TerrainPopulated", oldLevelChunk.terrainPopulated);
        ListTag listTag = new ListTag();
        for (int i = 0; i < 8; ++i) {
            int n;
            boolean bl = true;
            for (int j = 0; j < 16 && bl; ++j) {
                block3: for (int k = 0; k < 16 && bl; ++k) {
                    for (int i2 = 0; i2 < 16; ++i2) {
                        int n2 = j << 11 | i2 << 7 | k + (i << 4);
                        n = oldLevelChunk.blocks[n2];
                        if (n == 0) continue;
                        bl = false;
                        continue block3;
                    }
                }
            }
            if (bl) continue;
            byte[] arrby = new byte[4096];
            DataLayer dataLayer = new DataLayer();
            DataLayer dataLayer2 = new DataLayer();
            DataLayer dataLayer3 = new DataLayer();
            for (n = 0; n < 16; ++n) {
                for (int j = 0; j < 16; ++j) {
                    for (int k = 0; k < 16; ++k) {
                        int n3 = n << 11 | k << 7 | j + (i << 4);
                        byte by = oldLevelChunk.blocks[n3];
                        arrby[j << 8 | k << 4 | n] = (byte)(by & 0xFF);
                        dataLayer.set(n, j, k, oldLevelChunk.data.get(n, j + (i << 4), k));
                        dataLayer2.set(n, j, k, oldLevelChunk.skyLight.get(n, j + (i << 4), k));
                        dataLayer3.set(n, j, k, oldLevelChunk.blockLight.get(n, j + (i << 4), k));
                    }
                }
            }
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putByte("Y", (byte)(i & 0xFF));
            compoundTag2.putByteArray("Blocks", arrby);
            compoundTag2.putByteArray("Data", dataLayer.getData());
            compoundTag2.putByteArray("SkyLight", dataLayer2.getData());
            compoundTag2.putByteArray("BlockLight", dataLayer3.getData());
            listTag.add(compoundTag2);
        }
        compoundTag.put("Sections", listTag);
        compoundTag.putIntArray("Biomes", new ChunkBiomeContainer(registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), new ChunkPos(oldLevelChunk.x, oldLevelChunk.z), biomeSource).writeBiomes());
        compoundTag.put("Entities", oldLevelChunk.entities);
        compoundTag.put("TileEntities", oldLevelChunk.blockEntities);
        if (oldLevelChunk.blockTicks != null) {
            compoundTag.put("TileTicks", oldLevelChunk.blockTicks);
        }
        compoundTag.putBoolean("convertedFromAlphaFormat", true);
    }

    public static class OldLevelChunk {
        public long lastUpdated;
        public boolean terrainPopulated;
        public byte[] heightmap;
        public OldDataLayer blockLight;
        public OldDataLayer skyLight;
        public OldDataLayer data;
        public byte[] blocks;
        public ListTag entities;
        public ListTag blockEntities;
        public ListTag blockTicks;
        public final int x;
        public final int z;

        public OldLevelChunk(int n, int n2) {
            this.x = n;
            this.z = n2;
        }
    }
}

