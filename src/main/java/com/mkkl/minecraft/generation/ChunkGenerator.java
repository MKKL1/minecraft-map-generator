package com.mkkl.minecraft.generation;

import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.Region;
import com.mkkl.minecraft.TerrainManager;
import com.mkkl.minecraft.generation.features.DecorationGenerator;
import com.mkkl.minecraft.generation.surface.SurfaceGenerator;
import com.mkkl.types.TerrainHeightMap;

import java.util.Random;

public class ChunkGenerator {
    private final TerrainManager terrainManager;
    public ChunkGenerator(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public Chunk generateChunk(SurfaceGenerator surfaceGenerator, GenerationSettings generationSettings,
                               TerrainHeightMap terrainHeightMap, int xstart, int ystart) {

        return generateChunk(terrainManager.getRegionOrCreate(xstart/32, ystart/32),
                surfaceGenerator, generationSettings, terrainHeightMap, xstart, ystart);
    }
    public Chunk generateChunk(Region region, SurfaceGenerator surfaceGenerator, GenerationSettings generationSettings,
                               TerrainHeightMap terrainHeightMap, int xstart, int ystart) {

        GeneratorData generatorData = new GeneratorData();
        if (region.containsChunk(xstart%32, ystart%32)) {
            generatorData.chunk = region.getChunk(xstart%32, ystart%32);
        } else {
            generatorData.chunk = new Chunk();
            for(int i = 0; i < 16; i++) {
                for(int j = 0; j < 16; j++) {
                    generatorData.chunk.setMaxHeight(Math.round(terrainHeightMap.getPoint((xstart*16)+i,(ystart*16)+j) - terrainHeightMap.minheight));
                }
            }
            generatorData.chunk.initBlocks(generatorData.chunk.getMaxHeight()+20);
        }
        generatorData.chunk.setXPos(xstart);
        generatorData.chunk.setYPos(ystart);
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                generatorData.xInWorld = (xstart*16)+i;
                generatorData.yInWorld = (ystart*16)+j;
                generatorData.xInChunk = i;
                generatorData.yInChunk = j;

                surfaceGenerator.generateColumn(generationSettings,generatorData, terrainHeightMap);
            }
        }
        generatorData.chunk.generated = true;
        return generatorData.chunk;
    }

    public void decorateChunk(DecorationGenerator decorationGenerator, Chunk chunk) {
        decorationGenerator.generateTrees(chunk.getXPos(), chunk.getYPos());
        chunk.decorated = true;
    }

    public static Random createRandom(final long seed, final int x, final int y) {
        final Random random = new Random(seed);

        long long1 = random.nextLong();
        long long2 = random.nextLong();
        long newSeed = (long) x * long1 ^ (long) y * long2 ^ seed;
        random.setSeed(newSeed);

        return random;
    }
}
